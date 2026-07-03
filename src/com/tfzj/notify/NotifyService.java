package com.tfzj.notify;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.SOAPHeaderElement;

import net.sf.json.JSONObject;
import weaver.general.BaseBean;

import com.tfzj.notify.webservice.ISysNotifyTodoWebService;
import com.tfzj.notify.webservice.ISysNotifyTodoWebServiceProxy;
import com.tfzj.notify.webservice.ISysNotifyTodoWebServiceServiceLocator;
import com.tfzj.notify.webservice.ISysNotifyTodoWebServiceServiceSoapBindingStub;
import com.tfzj.notify.webservice.NotifyTodoAppResult;
import com.tfzj.notify.webservice.NotifyTodoRemoveContext;
import com.tfzj.notify.webservice.NotifyTodoSendContext;

public class NotifyService extends BaseBean{
	
	public String appName="行政管理平台";
	
	public String modelName="xz";
	
	/**
	 * 推送待办
	 * @param context
	 * @return
	 */
	public NotifyTodoAppResult sendTodo(NotifyTodoSendContext context){
		
		context.setAppName(appName);
		context.setModelName(modelName);
		
		NotifyTodoAppResult result=null;
		try {
			
			writeLog("[NotifyService.sendTodo] context:"+JSONObject.fromObject(context).toString());
			
			ISysNotifyTodoWebServiceProxy proxy=new ISysNotifyTodoWebServiceProxy();
			result=proxy.sendTodo(context);
			
		} catch (Exception e) {
			writeLog("[NotifyService.sendTodo] 待办推送失败，ERROR:"+e.toString());
			writeLog(e);
		}
		
		return result;
		
	}
	
	/**
	 * 待办变为已办
	 * @param context
	 * @return
	 */
	public NotifyTodoAppResult setTodoDone(NotifyTodoRemoveContext context){
		
		context.setAppName(appName);
		context.setModelName(modelName);
		
		NotifyTodoAppResult result=null;
		try {
			
			writeLog("[NotifyService.setTodoDone] context:"+JSONObject.fromObject(context).toString());
			
			ISysNotifyTodoWebServiceProxy proxy=new ISysNotifyTodoWebServiceProxy();
			result=proxy.setTodoDone(context);
			
		} catch (Exception e) {
			writeLog("[NotifyService.setTodoDone] 已办推送失败，ERROR:"+e.toString());
			writeLog(e);
		}
		
		return result;
		
	}
	
	/**
	 * 删除待办
	 * @param context
	 * @return
	 */
	public NotifyTodoAppResult deleteTodo(NotifyTodoRemoveContext context){
		
		context.setAppName(appName);
		context.setModelName(modelName);
		
		NotifyTodoAppResult result=null;
		try {
			
			writeLog("[NotifyService.deleteTodo] context:"+JSONObject.fromObject(context).toString());
			
			ISysNotifyTodoWebServiceProxy proxy=new ISysNotifyTodoWebServiceProxy();
			result=proxy.deleteTodo(context);
			
		} catch (Exception e) {
			writeLog("[NotifyService.deleteTodo] 待办删除失败，ERROR:"+e.toString());
			writeLog(e);
		}
		
		return result;
		
	}
	
	/**
	 * 添加用户验证
	 * @param _call
	 * @return
	 */
	public org.apache.axis.client.Call getCall(org.apache.axis.client.Call _call){
		
		SOAPHeaderElement header = new SOAPHeaderElement("http://webservice.notify.sys.kmss.landray.com/","RequestSOAPHeader");
        try {
        	SOAPElement ele = header.addChildElement("tns:user");
        	ele.addTextNode("osadmin");  
        	
        	ele = header.addChildElement("tns:password");
        	//ele.addTextNode("c4ca4238a0b923820dcc509a6f75849b");  //测试
        	
        	ele.addTextNode("62a199725aa4501ae2e2f7b3ce554940");  //正式
        	
        	
		} catch (SOAPException e) {
			e.printStackTrace();
		}
        _call.addHeader(header);
		
		return _call;
	}
	
	public static void main(String[] args) {
		
		/*
		NotifyService service=new NotifyService();
		NotifyTodoRemoveContext context=new NotifyTodoRemoveContext();
		context.setModelId("107");
		
		NotifyTodoAppResult result=service.deleteTodo(context);
		System.out.println("result:"+JSONObject.fromObject(result));
		*/
		
		/*
		try {
			ISysNotifyTodoWebServiceServiceLocator serviceLocator=new ISysNotifyTodoWebServiceServiceLocator();
			ISysNotifyTodoWebServiceServiceSoapBindingStub sub=(ISysNotifyTodoWebServiceServiceSoapBindingStub)serviceLocator.getISysNotifyTodoWebServicePort();
			
			sub.deleteTodo(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		
		JSONObject targetsObj=new JSONObject();
		targetsObj.put("LoginName", "likaicheng");
		
        NotifyService service=new NotifyService();
        NotifyTodoSendContext context=new NotifyTodoSendContext();
		
		context.setModelId("107");
        context.setSubject("xxxxxxx");
        context.setLink("http://192.168.144.118/com/tfzj/ssoLogin.jsp?action=todo&requestid=1111");
        context.setType(1);
        //context.setTargets("{\"LoginName\":\""+receiver+"\"}");
        context.setTargets(targetsObj.toString());
        context.setCreateTime("2018-11-02 12:00:00");
        context.setDocCreator(targetsObj.toString());
        
        NotifyTodoAppResult result=service.sendTodo(context);
        int returnstate=result.getReturnState();
        
		System.out.println(JSONObject.fromObject(result).toString());
		
	}
}
