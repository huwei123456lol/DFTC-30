package com.engine.dfxm.modeaction.fh;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.engine.dfxm.manager.PropertiesManager;
import com.engine.dfxm.util.DFXMModeUtil;
import com.engine.dfxm.util.TransUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * 发货数量校验接口，数量校验不通过不允许保存
 * @author wangkun
 * @date 2023-12-14 15:56
*/
public class FHSLJYAction extends AbstractModeExpandJavaCodeNew {
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
					HashMap<String,String> mainMap = new HashMap<String,String>();
					Property[] properties = requestInfo.getMainTableInfo().getProperty();// 获取表单主字段信息
					for (int i = 0; i < properties.length; i++) {
						String name = properties[i].getName().toLowerCase();// 主字段名称
						String value = Util.null2String(properties[i].getValue());// 主字段对应的值
						mainMap.put(name, value);
					}
					Map<String, Object> reMap= DFXMModeUtil.doCheckFHDSL(billid+"",mainMap.get("order_code"));
					if(!(boolean)reMap.get("status")){
						result.put("errmsg",(String) reMap.get("msg"));
						result.put("flag", "false");
					}
				}
			}
		} catch (Exception e) {
			result.put("errmsg","自定义出错信息");
			result.put("flag", "false");
		}
		return result;
	}

	private Map<String, Object> doSend360(String operateCode, String ids, String type, String doMsg) {
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
			String sql="";
			if(pmObj.containsKey("mainDataSql")){
				sql=pmObj.getString("mainDataSql")+ids;
			}else{
				sql="select  *  from "+modetablename+" where id="+ids+"";
			}
			RecordSet rs=new RecordSet();
			rs.executeQuery(sql);
			reMap.put("status",true);
			String msg="";
			JSONObject data=new JSONObject();
			JSONArray dataArr=new JSONArray();
			JSONArray mainData=pmObj.getJSONArray("mainData");
			String paramstr="";
			while (rs.next()){
				JSONObject mainParamData=new JSONObject();
				for (int i = 0; i < mainData.size(); i++) {
					JSONObject field=mainData.getJSONObject(i);
					TransUtil.setFieldValue(mainParamData,field,Util.null2String(rs.getString(field.getString("f"))));
				}
				if(!"".equals(tableto)){
					data.put(tableto,mainParamData);
					//if(pmObj.containsKey("detailData")){
					//    JSONArray detailData=pmObj.getJSONArray("detailData");
					//    for (int i = 0; i < detailData.size(); i++) {
					//        JSONObject detail=detailData.getJSONObject(i);
					//        JSONArray dtFieldData=detail.getJSONArray("dtFieldData");
					//        String fromTable=detail.getString("fromTable");
					//        String toTable=detail.getString("toTable");
					//        String mainPKFieldFrom=detail.getString("mainPKFieldFrom");
					//        String mainPKFieldTo=detail.getString("mainPKFieldTo");
					//        String detailSql="select * from "+modetablename+"_"+fromTable+" where "+mainPKFieldTo+"='"+rs.getString(mainPKFieldFrom)+"'";
					//        RecordSet detailRs=new RecordSet();
					//        detailRs.executeQuery(detailSql);
					//        JSONArray detailDataArr=new JSONArray();
					//        //获取多行数据
					//        while (detailRs.next()){
					//            JSONObject detailDataRow=new JSONObject();
					//            //遍历字段数组
					//            for (int j = 0; j < dtFieldData.size(); j++) {
					//                JSONObject field=dtFieldData.getJSONObject(j);
					//                TransUtil.setFieldValue(detailDataRow,field,Util.null2String(detailRs.getString(field.getString("f"))));
					//            }
					//            detailDataArr.add(detailDataRow);
					//        }
					//        //    同一个明细数据获取完毕，放入数据
					//        data.put(toTable,detailDataArr);
					//    }
					//}
					//paramstr=data.toString();
				}else{
					dataArr.add(mainParamData);
					//paramstr=dataArr.toString();
				}
				//paramstr=d.toString();
			}
			//JSONArray d=JSONArray.fromObject("[\n" +
			//		"    {\n" +
			//		"        \"DELIVERY_CODE\": \"202311200001\",\n" +
			//		"        \"DELIVERY_LINECODE\": \"1\",\n" +
			//		"        \"FROM_ORDER_CODE\": \"O202311010001\",\n" +
			//		"        \"FROM_ORDER_LINECODE\": \"1\",\n" +
			//		"        \"MATTER_CODE\": \"cdsccdc11\",\n" +
			//		"        \"MATTER_NAME\": \"焊接四角螺母\",\n" +
			//		"        \"SAP_MATTER_CODE\": \"1\",\n" +
			//		"        \"SUP_MATTER_CODE\": \"2\",\n" +
			//		"        \"NOTICE\": \"notice1\",\n" +
			//		"        \"PROCESS_STATE\": \"comprehensive_process1\",\n" +
			//		"        \"ICODE\": \"cdsccdc11147\",\n" +
			//		"        \"ZSJ_FLAG\": \"Y\",\n" +
			//		"        \"ZSJ_ICODE\": \"cdsccdc11147\",\n" +
			//		"        \"DELIVERY_BATCH\": \"202311030003\",\n" +
			//		"        \"DATA_SOURCE_EID\": \"BU_PUR_ORDER_DETAIL_4585e5fdeb1118r894626b47f4e591rt\",\n" +
			//		"        \"STYLIST_USERCODE\": \"2\",\n" +
			//		"        \"COLOR\": \"part_color1\",\n" +
			//		"        \"STANDARD_PRICE\": \"1\",\n" +
			//		"        \"STANDARD_ACCOUNT\": \"2\",\n" +
			//		"        \"SAP_ORDER_LINECODE\": \"1\",\n" +
			//		"        \"SAP_ORDER_CODE\": \"2\",\n" +
			//		"        \"FIRST_WEIGHT_FLAG\": \"Y\",\n" +
			//		"        \"CHECK_FLAG\": \"Y\",\n" +
			//		"        \"URGENT_FLAG\": \"1\",\n" +
			//		"        \"APPLY_CODE\": \"A202311200001\",\n" +
			//		"        \"PRO_CODE\": \"A0091\",\n" +
			//		"        \"PRO_NAME\": \"A0091\",\n" +
			//		"        \"ORDER_TYPE\": \"30\",\n" +
			//		"        \"SEND_WH_NAME\": \"A01\",\n" +
			//		"        \"DELIVERY_FROM\": \"1\",\n" +
			//		"        \"CHECK_TYPE\": \"2\",\n" +
			//		"        \"SUPPLIER_CODE\": \"1\",\n" +
			//		"        \"SUPPLIER_NAME\": \"2\",\n" +
			//		"        \"XTDD_FLAG\": \"1\",\n" +
			//		"        \"ZYS_USERCODE\": \"2\",\n" +
			//		"        \"SZZG_USERCODE\": \"1\",\n" +
			//		"        \"DELIVERY_STATE\": \"2\",\n" +
			//		"        \"OPTYPE\": \"1\",\n" +
			//		"        \"READFLAG\": \"1\",\n" +
			//		"        \"RECEIVE_MSG\": \"2\",\n" +
			//		"        \"ARRIVAL_TIME\": \"2023-11-20\",\n" +
			//		"        \"ORDER_NUM\": \"1\",\n" +
			//		"        \"SEND_NUM\": \"2\"\n" +
			//		"    }\n" +
			//		"]");
			//dataArr=d;
			if(dataArr.size()>0){
				//数据全部封装完毕，创建请求，推送数据
				log.info(doMsg+"推送报文: "+"  params:"+dataArr);
				HttpRequest request= HttpUtil.createPost(apiUrl);
				request.header("Authorization",password);
				request.header("Content-Type",contentType);
				request.body(paramstr);
				HttpResponse response=request.execute();
				if(response.isOk()){
					String ret=response.body();
					log.info(doMsg+"返回报文："+ret);
					JSONObject retJson=JSONObject.fromObject(ret);
					if("200".equals(retJson.getString("code"))){
						JSONObject retdata=retJson.getJSONObject("data");
						if("S".equals(retdata.getString("MESSAGE_TYPE"))){
							reMap.put("status",true);
							reMap.put("msg",doMsg+"成功，"+retdata.getString("MESSAGE"));
						}else{
							reMap.put("status",false);
							reMap.put("msg",doMsg+"失败，"+retdata.getString("MESSAGE"));
						}
					}else{
						reMap.put("status",false);
						reMap.put("msg",doMsg+"失败,"+response.body());
					}
				}else{
					reMap.put("status",false);
					reMap.put("msg",doMsg+"失败,"+response.body());
				}
			}
		}catch (Exception e){
			reMap.put("status",false);
			reMap.put("msg",doMsg+"失败,第三方接口异常："+e);
		}
		return reMap;
	}

}