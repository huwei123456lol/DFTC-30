package com.tfzj;

import java.util.HashMap;
import java.util.Map;

import com.tfzj.notify.NotifyService;
import com.tfzj.notify.webservice.NotifyTodoAppResult;
import com.tfzj.notify.webservice.NotifyTodoRemoveContext;
import com.tfzj.notify.webservice.NotifyTodoSendContext;

import net.sf.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.general.WorkFlowTransMethod;
import weaver.hrm.resource.ResourceComInfo;
import weaver.workflow.workflow.WorkTypeComInfo;
import weaver.workflow.workflow.WorkflowComInfo;

/**
 * 统一待办推送服务类，用于将待办任务推送到门户系统
 * @author feng
 *
 */
public class UnifiedTodoManager extends BaseBean {

	
	String resourceids=getPropValue("UnifiedTodo", "resourceids");
	
	public void doPush(String requestid){
		doPush("",requestid);
	}
	
	public void doPush(String userid,String requestid){
		
		//等待2秒 让workflow_todochange触发器执行完成
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean isExist=false;//是否存在当前操作用户,避免触发器执行滞后情况
		
		RecordSet recordSet=new RecordSet();
		
		String sql="select distinct resourceid from workflow_todochange where requestid="+requestid;
		
		writeLog("[UnifiedTodoManager.doPush] 同步 requestid:"+requestid);
		
		recordSet.execute(sql);
		while(recordSet.next()){
			
			String resourceid=recordSet.getString("resourceid");
			
			if(resourceid.equals("1")){ //系统管理员不同步
				continue;
			}
			
			if(resourceid.equals(userid)){
				isExist=true;
			}
			
			if(!(","+resourceids+",").contains(","+resourceid+",")){
				writeLog("[UnifiedTodoManager.doPushAll] resourceid:"+resourceid+" 不在同步范围内");
				continue;
			}
			
			writeLog("[UnifiedTodoManager.doPush] 同步 resourceid:"+resourceid+" requestid:"+requestid);
			
			boolean flag1=processDoneRequest(resourceid,requestid);
			boolean flag2=pushTodoList(resourceid,requestid);
			
		}
		
		if(!userid.equals("")&&!isExist){ //如果当前用户不在触发器用户表中，则单独进行同步一次
			
			writeLog("[UnifiedTodoManager.doPush] 同步 resourceid:"+userid+" requestid:"+requestid);
			
			boolean flag1=processDoneRequest(userid,requestid);
			boolean flag2=pushTodoList(userid,requestid);
			
		}
		
		
	}
	
	/**
	 * 同步所有人
	 */
	public void doPushAll(){
		
		String currenttime=TimeUtil.getCurrentTimeString();
		String changetime=TimeUtil.timeAdd(currenttime, -30);//当前时间向前推30秒，避免删除操作中的流程
		
		RecordSet recordSet2=new RecordSet();
		
		String sql="select distinct t4.resourceid,t4.requestid from( "+
		   		   "select t1.id as resourceid,t2.lasttime from (select id from hrmresource where status in(0,1,2,3)) t1 "+
		           "left join workflow_todoLasttime t2 on t1.id=t2.resourceid "+
		           ") t3,workflow_todochange t4 where t3.resourceid=t4.resourceid and (t3.lasttime is null or t3.lasttime<='"+changetime+"') ";
		recordSet2.execute(sql);
		
		writeLog("[UnifiedTodoManager.doPushAll] sql:"+sql);
		
		while(recordSet2.next()){
			
			String resourceid=recordSet2.getString("resourceid");
			String requestid=recordSet2.getString("requestid");
			
			if(resourceid.equals("1")){
				continue;
			}
			

			
			writeLog("[UnifiedTodoManager.doPushAll] 同步 resourceid:"+resourceid+" requestid："+requestid+" 开始");
			
			boolean flag1=processDoneRequest(resourceid,requestid);
			boolean flag2=pushTodoList(resourceid,requestid);
			
			writeLog("[UnifiedTodoManager.doPushAll] resourceid:"+resourceid+" requestid:"+requestid+" flag1:"+flag1+" flag2:"+flag2);
			

				delTodoChange(resourceid, requestid);
				setSyncLasttime(resourceid,changetime);
			
			if((","+resourceids+",").contains(","+resourceid+",")){
				writeLog("[UnifiedTodoManager.doPushAll] 同步 resourceid:"+resourceid+" requestid："+requestid+" 结束");
			}
		}	
		
	}
	
	/**
	 * 删除统一代办流程
	 * @param flowid
	 */
	public boolean deleteRequestInfo(String flowid){
		
		boolean isDelete=false;
		try {
	        
	        
	        NotifyService service=new NotifyService();
	        NotifyTodoRemoveContext context=new NotifyTodoRemoveContext();
	        
	        context.setModelId(flowid);
	        context.setOptType(1);
	        
	        NotifyTodoAppResult result=service.deleteTodo(context);
	        int returnstate=result.getReturnState();
	        
	        writeLog("[UnifiedTodoManager.deleteRequestInfo] requestData:"+JSONObject.fromObject(result).toString());
	        
	        if(returnstate==2){
	        	isDelete=true;
	        }else {
	        	//writeLog("[UnifiedTodoManager.deleteRequestInfo] requestData:"+JSONObject.fromObject(result).toString());
			}
			
		} catch (Exception e) {
			writeLog("[UnifiedTodoManager.deleteRequestInfo] 待办删除失败，ERROR:"+e.toString());
			writeLog(e);
		}
		
		return isDelete;
	}
	
	public boolean processDoneRequest(String resourceid,String requestid){
		
		
		boolean flag=true;
		
		try {
			
			WorkflowComInfo workflowComInfo = new WorkflowComInfo();
			ResourceComInfo resourceComInfo = new ResourceComInfo();
			
			RecordSet recordSet=new RecordSet();
			RecordSet recordSet2=new RecordSet();
			
			String todosql=getTodoSql(resourceid,requestid);
			String sql="select t1.*,t2.requestname,t2.workflowid,t2.currentnodeid,case when t2.requestid is not null then 0 else 1 end as isdone " +
					   "from (select * from workflow_todopush where resourceid="+resourceid+" and status=0) t1 " +
					   "left join ("+todosql+") t2 on t1.requestid=t2.requestid "+(requestid.equals("")?"":(" where t1.requestid="+requestid));
			recordSet.execute(sql);
			
			writeLog("[UnifiedTodoManager.processDoneRequest] sql:"+sql);
			
			while(recordSet.next()){
				
				String isdone=recordSet.getString("isdone");
				
				String flowid=recordSet.getString("requestid");
				
				String requestname="";
				String workflowname="";
				String nodename=recordSet.getString("nodename");
				
				sql="select * from workflow_requestbase where requestid="+flowid;
				recordSet2.executeSql(sql);
				
				if(recordSet2.next()){
					requestname=recordSet2.getString("requestname");
					workflowname=workflowComInfo.getWorkflowname(recordSet2.getString("workflowid"));
				}
				
				//流程不存在
				if(requestname.equals("")||workflowname.equals("")){
					
					boolean isDelete=deleteRequestInfo(flowid);//流程不存在则删除
					if(isDelete){
						sql="delete from workflow_todopush where resourceid="+resourceid+" and requestid="+flowid;
						recordSet2.executeSql(sql);
					}else{
						flag=false;
					}
					continue;
				}
				
				//在代办中，就不做处理
				if(isdone.equals("0")){
					
					writeLog("[UnifiedTodoManager.processDoneRequest] 暂未处理 resourceid:"+resourceid+" requestid:"+flowid+" requestname:"+requestname+" isdone:"+isdone);
					
					continue;
				}
				
				String receiver=resourceComInfo.getLoginID(resourceid);
		        
		        NotifyService service=new NotifyService();
		        NotifyTodoRemoveContext context=new NotifyTodoRemoveContext();
		        
		        context.setModelId(flowid);
		        context.setOptType(2);
		        context.setTargets("{\"LoginName\":\""+receiver+"\"}");
		        
		        NotifyTodoAppResult result=service.setTodoDone(context);
		        int returnstate=result.getReturnState();
		        
		        writeLog("[UnifiedTodoManager.processDoneRequest] requestData:"+JSONObject.fromObject(result).toString());
		        
		        if(returnstate==2){
		        	sql="update workflow_todopush set status=1 where resourceid="+resourceid+" and requestid="+flowid;
		        	recordSet2.execute(sql);
		        	
		        	writeLog("[UnifiedTodoManager.processDoneRequest] sql2:"+sql);
		        	
		        }else{
		        	//writeLog("[UnifiedTodoManager.processDoneRequest] requestData:"+requestData.toString());
		        	flag=false;
		        }
		        
			}
			
			
		}catch (Exception e) {
			flag=false;
			writeLog("[UnifiedTodoManager.processDoneRequest] 处理失败 ERROR:"+e.toString());
			writeLog(e);
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean pushTodoList(String resourceid,String requestid){
		
		
		boolean flag=true;
		
		try {
			
			WorkflowComInfo workflowComInfo = new WorkflowComInfo();
			WorkTypeComInfo workflowTypeComInfo = new WorkTypeComInfo();
			ResourceComInfo resourceComInfo = new ResourceComInfo();
			
			WorkFlowTransMethod workFlowTransMethod=new WorkFlowTransMethod();
			
			RecordSet recordSet=new RecordSet();
			RecordSet recordSet2=new RecordSet();
			
			String todosql=getTodoSql(resourceid,requestid);
			String sql="select t1.*,case when t2.requestid is not null then 1 else 0 end as ispush from ("+todosql+") t1 left join (select * from workflow_todopush where resourceid="+resourceid+" and status=0) t2 on t1.requestid=t2.requestid ";
			
			writeLog("[UnifiedTodoManager.pushTodoList] sql:"+sql);
			
			recordSet.execute(sql);
			
			writeLog("[UnifiedTodoManager.pushTodoList] getCounts:"+recordSet.getCounts());
			
			if(recordSet.getCounts()>0){
			
				while(recordSet.next()){
					
					String flowid=recordSet.getString("requestid");
					String requestname=recordSet.getString("requestname");
					String ispush=recordSet.getString("ispush");
					
					
					
					if(ispush.equals("1")){
						writeLog("[UnifiedTodoManager.pushTodoList] resourceid:"+resourceid+" requestid:"+requestid+" requestname:"+requestname+" 已经推送过");
						continue;
					}
					
					String workflowname=workflowComInfo.getWorkflowname(recordSet.getString("workflowid"));
					String nodename=workFlowTransMethod.getCurrentNode(recordSet.getString("currentnodeid"));
					String creator=resourceComInfo.getLoginID(recordSet.getString("creater"));
					String createdatetime=recordSet.getString("createdate")+" "+recordSet.getString("createtime");
					String receivedatetime=recordSet.getString("receivedate")+" "+recordSet.getString("receivetime");
					
					String receiver=resourceComInfo.getLoginID(resourceid);
			        
					//receiver="likaicheng";
					
					JSONObject targetsObj=new JSONObject();
					targetsObj.put("LoginName", receiver);
					
			        NotifyService service=new NotifyService();
			        NotifyTodoSendContext context=new NotifyTodoSendContext();
			        
			        context.setModelId(flowid);
			        context.setSubject(requestname);
			        context.setLink("http://tfxz.tfzq.com/com/tfzj/ssoLogin.jsp?action=todo&requestid="+flowid);
			        context.setType(1);
			        context.setTargets("{\"LoginName\":\""+receiver+"\"}");
			        context.setCreateTime(createdatetime);
			        context.setDocCreator("{\"LoginName\":\""+creator+"\"}");
			        
			        NotifyTodoAppResult result=service.sendTodo(context);
			        int returnstate=result.getReturnState();
			        
			        writeLog("[UnifiedTodoManager.pushTodoList] requestData:"+JSONObject.fromObject(result).toString());
			        
			        if(returnstate==2){
			        	
			        	sql="select * from workflow_todopush where resourceid="+resourceid+" and requestid="+flowid;
			        	recordSet2.execute(sql);
			        	if(recordSet2.next()){
			        		sql="update workflow_todopush set status=0,nodename='"+nodename+"' where resourceid="+resourceid+" and requestid="+flowid;
				        	recordSet2.execute(sql);
			        	}else{
			        		sql="insert into workflow_todopush(resourceid,requestid,nodename,status) values("+resourceid+","+flowid+",'"+nodename+"',0)";
				        	recordSet2.execute(sql);
			        	}
			        	
			        	writeLog("[UnifiedTodoManager.pushTodoList] sql2:"+sql);
			        	
			        	
			        }else{
			        	flag=false;
			        }
				}
				
			}
			
			
		} catch (Exception e) {
			flag=false;
			writeLog("[UnifiedTodoManager.pushTodoList] 处理失败 ERROR:"+e.toString());
			writeLog(e);
			e.printStackTrace();
		}
		
		return flag;
		
	}
	
	public String getTodoSql(String resourceid,String requestid){
		
		String select = " select distinct ";
		String fields = " t1.createdate,t1.createtime,t1.creater,t1.currentnodeid,t1.currentnodetype,t1.lastoperator,t1.creatertype,t1.lastoperatortype,t1.lastoperatedate,t1.lastoperatetime,t1.requestid,t1.requestname,t1.requestlevel,t1.workflowid,t2.receivedate,t2.receivetime ";
		String from = " from workflow_requestbase t1,workflow_currentoperator t2 ";
		String where = " where t1.requestid=t2.requestid "+(requestid.equals("")?"":(" and t1.requestid="+requestid));
		where += " and t2.usertype = 0 and t2.userid = " + resourceid;
		where += " and t2.isremark in( '0','1','5','7','8','9') and t2.islasttimes=1 and t1.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
		
		String sql=select+fields+from+where;
		
		return sql;
		
	}
	
	/**
	 * 已办、办结 sql
	 * @param resourceid
	 * @return
	 */
	public String getHandelSql(String resourceid){
		
		String select = " select distinct ";
		String fields = " t1.createdate,t1.createtime,t1.creater,t1.currentnodeid,t1.currentnodetype,t1.lastoperator,t1.creatertype,t1.lastoperatortype,t1.lastoperatedate,t1.lastoperatetime,t1.requestid,t1.requestname,t1.requestlevel,t1.workflowid,t2.receivedate,t2.receivetime ";
		String from = " from workflow_requestbase t1,workflow_currentoperator t2 ";
		String where = " where t1.requestid=t2.requestid ";
		where += " and t2.usertype = 0 and t2.userid = " + resourceid;
		where += " and t2.isremark in('2','4') and t2.islasttimes=1 ";
		
		String sql=select+fields+from+where;
		
		return sql;
		
	}
	
	/**
	 * 获取流程接收人
	 * @param requestid
	 * @return
	 */
	public String getReceiverids(String requestid){
		
		RecordSet recordSet2=new RecordSet();
		
		String receiver="";
		recordSet2.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid,isremark from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);
		while(recordSet2.next()){
			receiver+=","+recordSet2.getString("userid");
		}
		receiver=receiver.length()>0?receiver.substring(1):"";
		
		return receiver;
	}
	
	
	/**
	 * 删除流程更改记录
	 * @param resourceid 流程操作人
	 * @param requestid  流程id
	 * @return
	 */
	public void delTodoChange(String resourceid,String requestid){
		
		RecordSet recordSet2=new RecordSet();
		recordSet2.executeSql("delete from workflow_todochange where resourceid="+resourceid+" and requestid = " + requestid);
		
	}
	
	
	/**
	 * 获取最后更新时间
	 * @return
	 */
	public String getSyncLasttime(String resourceid){
		
		String lasttime="";
		RecordSet recordSet=new RecordSet();
		String sql="select * from workflow_todoLasttime where resourceid='"+resourceid+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			lasttime=recordSet.getString("lasttime");
		}
		
		return lasttime;
	}
	
	/**
	 * 更新最后更新时间
	 * @return
	 */
	public void setSyncLasttime(String resourceid,String lasttime){
		
		RecordSet recordSet=new RecordSet();
		String sql="select * from workflow_todoLasttime where resourceid='"+resourceid+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			sql="update workflow_todoLasttime set lasttime='"+lasttime+"' where resourceid='"+resourceid+"'";
		}else{
			sql="insert into workflow_todoLasttime(resourceid,lasttime) values('"+resourceid+"','"+lasttime+"')";
		}
		recordSet.execute(sql);
		
	}
	
	public String getWorkcode(String resourceid){
		String workcode="";
		RecordSet recordSet=new RecordSet();
		String sql="select * from hrmresource where id="+resourceid;
		recordSet.execute(sql);
		if(recordSet.next()){
			workcode=recordSet.getString("workcode");
		}
		return workcode;
	}
	
	public void writeLog(String logstr){
		
		String showLog=getPropValue("Portal", "showLog");
		if(showLog.equals("1")||true){
            super.writeLog(logstr);
        }
		
	}
	
	
}
