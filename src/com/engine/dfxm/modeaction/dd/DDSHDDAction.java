package com.engine.dfxm.modeaction.dd;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.util.TransUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * 订单收货地点更新
 * @author wangkun
 * @date 2023-12-14 15:56
*/
public class DDSHDDAction extends AbstractModeExpandJavaCodeNew {
	private Logger log= LoggerFactory.getLogger();


	/**
	 * 执行模块扩展动作
	 * @param param
	 *  param包含(但不限于)以下数据
	 *  user 当前用户
	 *  importtype 导入方式(仅在批量导入的接口动作会传输) 1 追加，2覆盖,3更新，获取方式(int)param.get("importtype")
     *  导入链接中拼接的特殊参数(仅在批量导入的接口动作会传输)，比如a=1，可通过param.get("a")获取参数值
	 *  页面链接拼接的参数，比如b=2,可以通过param.get("b")来获取参数
	 * @return 
	 */
	@Override
	public Map<String, String> doModeExpand(Map<String, Object> param) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			User user = (User)param.get("user");
			int billid = -1;//数据id
			int modeid = -1;//模块id
			RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
			if(requestInfo!=null){
				billid = Util.getIntValue(requestInfo.getRequestid());
				modeid = Util.getIntValue(requestInfo.getWorkflowid());
				if(billid>0&&modeid>0){
					Map<String, Object> reMap360=doSend360("360DDSHDD",billid+"","","订单收货地点更新(360)");
					if(!(boolean) reMap360.get("status")){
						result.put("flag", "false");
						result.put("errmsg",(String) reMap360.get("msg"));
					}
				}
			}
		} catch (Exception e) {
			result.put("errmsg","自定义出错信息");
			result.put("flag", "false");
		}
		return result;
	}


	/**
	 * 推送收货地址到360
	 * @author wangkun
	 * @date 2024-10-16 17:04
	 * @param operateCode
	 * @param ids
	 * @param type
	 * @param doMsg
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	private Map<String, Object> doSend360(String operateCode, String ids,String type,String doMsg) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			PropertiesManager pm=new PropertiesManager(operateCode,1);
			JSONObject pmObj=pm.getFieldValueObj();
			String password="";
			String contentType="";
			String apiUrl="";
			if(pmObj.containsKey("urlCode")){
				String urlCode=pmObj.getString("urlCode");
				PropertiesManager apipm=new PropertiesManager(urlCode,1);
				JSONObject apipmObj=apipm.getFieldValueObj();
				password=apipmObj.getString("token");
				contentType=apipmObj.getString("contentType");
				apiUrl=apipmObj.getString("url")+pmObj.getString("api");
			}else{
				password=pmObj.getString("token");
				contentType=pmObj.getString("contentType");
				apiUrl=pmObj.getString("url");
			}
			//表
			String modetablename=pmObj.getString("modetablename");
			String tableto=pmObj.getString("tableto");
			String sql="select  *  from "+modetablename+" where id="+ids+"";
			RecordSet rs=new RecordSet();
			rs.executeQuery(sql);
			reMap.put("status",true);
			String msg="";
			JSONObject data=new JSONObject();
			JSONArray mainData=pmObj.getJSONArray("mainData");
			JSONArray allDetailDataArr=new JSONArray();
			if (rs.next()){
				JSONObject mainParamData=new JSONObject();
				if(mainData.size()>0){
					for (int i = 0; i < mainData.size(); i++) {
						JSONObject field=mainData.getJSONObject(i);
						TransUtil.setFieldValue(mainParamData,field,Util.null2String(rs.getString(field.getString("f"))));
					}
					data.put(tableto,mainParamData);
				}
				if(pmObj.containsKey("detailData")){
					JSONArray detailData=pmObj.getJSONArray("detailData");
					for (int i = 0; i < detailData.size(); i++) {
						JSONObject detail=detailData.getJSONObject(i);
						JSONArray dtFieldData=detail.getJSONArray("dtFieldData");
						String fromTable=detail.getString("fromTable");
						String toTable=detail.getString("toTable");
						String mainPKFieldFrom=detail.getString("mainPKFieldFrom");
						String mainPKFieldTo=detail.getString("mainPKFieldTo");
						String detailSql="select * from "+modetablename+"_"+fromTable+" where "+mainPKFieldTo+"='"+rs.getString(mainPKFieldFrom)+"'";
						RecordSet detailRs=new RecordSet();
						detailRs.executeQuery(detailSql);
						JSONArray detailDataArr=new JSONArray();
						//获取多行数据
						while (detailRs.next()){
							JSONObject detailDataRow=new JSONObject();
							//遍历字段数组
							for (int j = 0; j < dtFieldData.size(); j++) {
								JSONObject field=dtFieldData.getJSONObject(j);
								if(field.getString("f").startsWith("@")){
									TransUtil.setFieldValue(detailDataRow,field,Util.null2String(rs.getString(field.getString("f").replaceFirst("@",""))));
								}else{
									TransUtil.setFieldValue(detailDataRow,field,Util.null2String(detailRs.getString(field.getString("f"))));
								}
							}
							detailDataArr.add(detailDataRow);
						}
						allDetailDataArr.add(detailDataArr);
					}
				}
				if(allDetailDataArr.size()>0){
					//数据全部封装完毕，创建请求，推送数据
					log.info(doMsg+"推送报文: "+"  params:"+allDetailDataArr.getJSONArray(0));
					//if(true){
					//	reMap.put("status",false);
					//	reMap.put("msg","11111");
					//	return reMap;
					//}
					HttpRequest request=HttpUtil.createPost(apiUrl);
					request.header("Authorization",password);
					request.header("Content-Type",contentType);
					request.body(allDetailDataArr.getJSONArray(0).toString());
					HttpResponse response=request.execute();
					if(response.isOk()){
						String ret=response.body();
						log.info(doMsg+"返回报文："+ret);
						JSONObject retJson=JSONObject.fromObject(ret);
						if("S".equals(retJson.getString("MESSAGE_TYPE"))){
							reMap.put("status",true);
							reMap.put("msg",doMsg+"成功，"+retJson.getString("MESSAGE"));
						}else{
							reMap.put("status",false);
							reMap.put("msg",doMsg+"失败，"+retJson.getString("MESSAGE"));
						}
					}else{
						reMap.put("status",false);
						reMap.put("msg",doMsg+"失败,"+response.body());
					}
				}else{
					reMap.put("status",false);
					reMap.put("msg",doMsg+"数据不存在，请确认数据是否正确！");
				}
			}else{
				reMap.put("status",false);
				reMap.put("msg",doMsg+"数据不存在，请确认数据是否正确！");
			}
		}catch (Exception e){
			reMap.put("status",false);
			reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
		}
		return reMap;
	}

}