package com.dfjszx.esbcode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.cube.service.impl.ModCardServiceImpl;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.schedule.ext.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @Author GodWei
 * @Date 2023-09-27
 * @Description
 * @Motto Good Good Study! Day Day Up!
 */
public class SyncSupRiskQuartz {

	private BaseBean log=new BaseBean();


	public Map execute(Map param){

		Map<String,String> ret=new HashMap<>();

		ret.put("errmsg","");
		ret.put("errList","");
		ret.put("flag", "true");
		ret.put("status", "true");
		JSONArray riskListData = new JSONArray();
		JSONArray riskListDtInfo = new JSONArray();

		try {

			RecordSet rs=new RecordSet();
			RecordSet rds=new RecordSet();

			// 构建数据权限
			ModeRightInfo modeRightInfo = new ModeRightInfo();
			modeRightInfo.setNewRight(true);

			ModCardServiceImpl modCardService=new ModCardServiceImpl();

			int index=0;
			String modedatacreatedate = TimeUtil.getCurrentDateString();

			//String sql="select * from uf_cggl_gysjbxxb where gysmc='上海泛微网络科技股份有限公司'";
			String sql="select * from uf_cggl_gysjbxxb where gyszt in (1,2,7,8)";
			log.writeLog("[SyncSupRiskQuartz.execute]sql:"+sql);
			rs.execute(sql);
			while(rs.next()){

				String gysmc=rs.getString("gysmc");
				String xggys=rs.getString("id");

				String body=getRiskInfo(gysmc);

				JSONObject json=JSONObject.parseObject(body);
				riskListData.add(json);
				log.writeLog("result ==" + body);

				String __STATUSCODE=json.getString("__STATUSCODE");

				log.writeLog("[SyncSupRiskQuartz.execute]gysmc:"+gysmc+" STATUSCODE:"+__STATUSCODE);

				if(__STATUSCODE.equals("1")){

					index++;

					String data=json.getString("data");

					JSONObject dataObj=JSONObject.parseObject(data);

					String riskLevel=dataObj.getString("riskLevel");

					JSONArray riskList=dataObj.getJSONArray("riskList");

					for(int i=0;i<riskList.size();i++){

						JSONObject riskInfo=riskList.getJSONObject(i);

						String count=riskInfo.getString("count");
						String name=riskInfo.getString("name");
						String type=riskInfo.getString("type");

						JSONArray list=riskInfo.getJSONArray("list");

						for(int m=0;m<list.size();m++){

							JSONObject detailInfo=list.getJSONObject(m);

							String total=detailInfo.getString("total");
							String tag=detailInfo.getString("tag");
							String type1=detailInfo.getString("type");
							String title=detailInfo.getString("title");

							JSONArray finallist=detailInfo.getJSONArray("list");

							for(int n=0;n<finallist.size();n++){

								JSONObject finalInfo=finallist.getJSONObject(n);

								String companyId=finalInfo.getString("id");
								String companyName=finalInfo.getString("companyName");
								String riskCount=finalInfo.getString("riskCount");
								String title2=finalInfo.getString("title");
								String type2=finalInfo.getString("type");
								String desc1=finalInfo.getString("desc");

								sql="select * from uf_gys_fxxx where companyId='"+companyId+"'";
								log.writeLog("[SyncSupRiskQuartz.execute]sql:"+sql);
								rds.execute(sql);
								if(!rds.next()){

									Map<String,Object> paramsMap=new HashMap<>();

									paramsMap.put("type","1");
									paramsMap.put("modeId","213502");
									paramsMap.put("formId","-1656");
									paramsMap.put("src","submit");
									paramsMap.put("layoutid","1212502");
									paramsMap.put("isFormMode","1");
									paramsMap.put("iscreate","1");
									paramsMap.put("currentLayoutId","1212502");
									paramsMap.put("pageexpandid","1321086");
									paramsMap.put("issystemflag","1");
									paramsMap.put("oldmodedatastatus","0");

									JSONObject jsonStr=new JSONObject(true);

									jsonStr.put("field1717594",xggys);
									//jsonStr.put("field1717598",riskLevel);
									jsonStr.put("field1717608",count);
									jsonStr.put("field1717599",name);
									jsonStr.put("field1717601",type);
									jsonStr.put("field1717609",total);
									jsonStr.put("field1717600",tag);
									jsonStr.put("field1717606",type1);
									jsonStr.put("field1717605",title);
									jsonStr.put("field1717602",companyId);
									jsonStr.put("field1717603",companyName);
									jsonStr.put("field1717604",riskCount);
									jsonStr.put("field1717597",title2);
									jsonStr.put("field1717593",type2);
									jsonStr.put("field1717596",desc1);
									jsonStr.put("field1717595",modedatacreatedate);
									riskListDtInfo.add(jsonStr);
									paramsMap.put("JSONStr",jsonStr.toJSONString());
									User user=new User(1);
									String username=user.getLastname();
									log.writeLog("[SyncSupRiskQuartz.execute]name:" + username);
									log.writeLog("[SyncSupRiskQuartz.execute]paramsMap:" + paramsMap);

									Map<String,Object> resultMap=modCardService.doSubmit(paramsMap,user);

									log.writeLog("[SyncSupRiskQuartz.execute]resultMap:" + resultMap.toString());

                                    /*

                                    String modedatacreatedate = TimeUtil.getCurrentDateString();
                                    String modedatacreatetime = TimeUtil.getOnlyCurrentTimeString();

                                    String modeuuid = UUID.randomUUID().toString();

                                    sql = "insert into uf_gys_fxxx(formmodeid,modeuuid,modedatacreater," +
                                            "modedatacreatertype,modedatacreatedate,modedatacreatetime,xggys," +
                                            "riskLevel,count,name,type,total,tag,type1,title,companyId,companyName," +
                                            "riskCount,title2,type2,desc1) " +
                                            "values('" + formmodeid + "','" + modeuuid + "','1','0','" + modedatacreatedate + "'," +
                                            "'" + modedatacreatetime + "','"+xggys+"','"+riskLevel+"','"+count+"'," +
                                            "'"+name+"','"+type+"','"+total+"','"+tag+"','"+type1+"','"+title+"'," +
                                            "'"+companyId+"','"+companyName+"','"+riskCount+"','"+title2+"','"+type2+"','"+desc1+"')";
                                    log.writeLog("[SyncSupRiskQuartz.execute]sql:" + sql);

                                    boolean flag = rds.execute(sql);

                                    if (flag) {

                                        sql = "select * from uf_gys_fxxx where modeuuid='" + modeuuid + "'";
                                        rds.execute(sql);
                                        if (rds.next()) {
                                            int mainid = rds.getInt("id");
                                            modeRightInfo.editModeDataShare(1, formmodeid, mainid);
                                        }
                                    }
                                    */
								}

							}
						}


					}


				}



			}

			log.writeLog("[SyncSupRiskQuartz.execute]index："+index);
			ret.put("errmsg","index："+index);

		} catch (Exception e) {
			e.printStackTrace();
			ret.put("errmsg",e.toString());
			ret.put("errList",Arrays.asList(e.getStackTrace())+"");
			ret.put("flag", "true");
			ret.put("status", "false");
			log.writeLog("[SyncSupRiskQuartz.execute]err:"+e.toString());
			log.writeLog("[SyncSupRiskQuartz.execute]"+ Arrays.asList(e.getStackTrace()));
		}finally {
			log.writeLog("[SyncSupRiskQuartz.execute]ret:"+ret.toString());
		}
		ret.put("riskList",JSONArray.toJSONString(riskListData));
		ret.put("riskListInfo",JSONArray.toJSONString(riskListDtInfo));
		return ret;

	}


	public String getRiskInfo(String keyword){
		String result="";
		try {

			String url="https://api.dfmc.com.cn/mocs-bdp-ds/tianyan/valueAdd/riskInfo";

			String X_App_Id="10330";
			String X_Timestamp=String.valueOf(System.currentTimeMillis());
			String X_Sequence_No=TimeUtil.getCurrentTimeString().replaceAll("-","").replaceAll(":","").replaceAll(" ","")+"0001";
			String X_Signature= Base64.getEncoder().encodeToString("9a6b265797874b9c815073633dd69c61:a3eeca65d15d482a".getBytes());

			Map<String,String> header=new HashMap<>();

			header.put("apikey","qQE21dv2AjPE70Dc");
			header.put("X-App-Id",X_App_Id);
			header.put("X-Timestamp",X_Timestamp);
			header.put("X-Sequence-No",X_Sequence_No);
			header.put("X-Signature","Basic "+X_Signature);
			header.put("Authorization","Basic "+X_Signature);

			JSONObject params=new JSONObject(true);

			params.put("keyword",keyword);

			result= HttpUtil.doPostForJson(url,params.toJSONString(),header);

		}catch(Exception e){
			e.printStackTrace();
			log.writeLog("[SyncSupRiskQuartz.getRiskInfo]err:"+e.toString());
			result = e.toString();
		}
		return result;
	}

}
