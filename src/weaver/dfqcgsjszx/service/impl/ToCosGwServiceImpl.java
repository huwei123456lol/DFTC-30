package weaver.dfqcgsjszx.service.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.service.ToCosGwService;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
/**
 * 获取cos公文信息
 * @author peixuan
 *
 */
public class ToCosGwServiceImpl implements ToCosGwService {

	/**
	 * 获取公文基础信息
	 * 
	 * @param message 公文基础信息字符串（json格式）
	 * @return
	 */
	public String toCosGwMessage(String message) {

		JSONObject json = JSONObject.fromObject(message);
		RecordSet rs = new RecordSet();
		new BaseBean().writeLog("接收的公文基础信息=====>" + json.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			rs.execute("select * from uf_cosgw where docinfoid = '"
					+ json.getString("DocInfoId") + "'");
			if (rs.next()) {
				new RecordSet().execute("update uf_cosgw set DocNoKey = '"
						+ json.getString("DocNoKey") + "',DocDateYear = '"
						+ json.getString("DocDateYear") + "',DispatchNo = '"
						+ json.getString("DispatchNo") + "',DocNo = '"
						+ json.getString("DocNo") + "',DocNoCode = '"
						+ json.getString("DocNoCode") + "',DocClassCode = '"
						+ json.getString("DocClassCode") + "',DocClassName = '"
						+ json.getString("DocClassName") + "',DocCharacter = '"
						+ json.getString("DocCharacter")
						+ "',DocCharacterName = '"
						+ json.getString("DocCharacterName") + "',DocType = '"
						+ json.getString("DocType") + "',DocTitle = '"
						+ json.getString("DocTitle") + "',CompanyName = '"
						+ json.getString("CompanyName") + "',CompanyCode = '"
						+ json.getString("CompanyCode") + "',UrgencyGrade = '"
						+ json.getString("UrgencyGrade")
						+ "',UrgencyGradeName = '"
						+ json.getString("UrgencyGradeName")
						+ "',SecurityLevel = '"
						+ json.getString("SecurityLevel")
						+ "',SecurityLevelName = '"
						+ json.getString("SecurityLevelName") + "',KeyWord = '"
						+ json.getString("KeyWord") + "',MainTo = '"
						+ json.getString("MainTo") + "',CoypTo = '"
						+ json.getString("CoypTo") + "',BackContent = '"
						+ json.getString("BackContent") + "',Creater = '"
						+ json.getString("Creater") + "',CreaterId = '"
						+ json.getString("CreaterId") + "',CreaterUnitName = '"
						+ json.getString("CreaterUnitName")
						+ "',CreaterUnitCode = '"
						+ json.getString("CreaterUnitCode")
						+ "',CreateDate = '" + json.getString("CreateDate")
						+ "',CreaterTelNo = '" + json.getString("CreaterTelNo")
						+ "',QFAccount = '" + json.getString("QFAccount")
						+ "',QFName = '" + json.getString("QFName")
						+ "',QFDate = '" + json.getString("QFDate")
						+ "',ReadCompanyCode = '"
						+ json.getString("ReadCompanyCode")
						+ "',ReadSubUnitCode = '"
						+ json.getString("ReadSubUnitCode")
						+ "',SendCompanyCode = '"
						+ json.getString("SendCompanyCode")
						+ "',SendCompanyName = '"
						+ json.getString("SendCompanyName")
						+ "',SendPersonId = '" + json.getString("SendPersonId")
						+ "',SendPerson = '" + json.getString("SendPerson")
						+ "',SendUnitName = '" + json.getString("SendUnitName")
						+ "',SendUnitCode = '" + json.getString("SendUnitCode")
						+ "',PrintSendDate = '"
						+ json.getString("PrintSendDate") + "',FileInfo = '"
						+ json.getString("FileInfo") + "',FileCount = '"
						+ json.getString("FileCount") + "',VerIndex = '"
						+ json.getString("VerIndex")
						+ "',ProcedureFeedBack = '"
						+ json.getString("ProcedureFeedBack")
						+ "',AuthCode = '" + json.getString("AuthCode")
						+ "',OperatorState = '"
						+ json.getString("OperatorState")
						+ "' where DocInfoId = '" + json.getString("DocInfoId")
						+ "'");
			} else {

				new RecordSet()
						.execute("insert into uf_cosgw(DocInfoId,DocNoKey,DocDateYear,DispatchNo,DocNo,DocNoCode,DocClassCode,DocClassName,DocCharacter,DocCharacterName,DocType,DocTitle,CompanyName,CompanyCode,UrgencyGrade,UrgencyGradeName,SecurityLevel,SecurityLevelName,KeyWord,MainTo,CoypTo,BackContent,Creater,CreaterId,CreaterUnitName,CreaterUnitCode,CreateDate,CreaterTelNo,QFAccount,QFName,QFDate,ReadCompanyCode,ReadSubUnitCode,SendCompanyCode,SendCompanyName,SendPersonId,SendPerson,SendUnitName,SendUnitCode,PrintSendDate,FileInfo,FileCount,VerIndex,ProcedureFeedBack,AuthCode,OperatorState) "
								+ "values('"
								+ json.getString("DocInfoId")
								+ "','"
								+ json.getString("DocNoKey")
								+ "','"
								+ json.getString("DocDateYear")
								+ "','"
								+ json.getString("DispatchNo")
								+ "','"
								+ json.getString("DocNo")
								+ "','"
								+ json.getString("DocNoCode")
								+ "','"
								+ json.getString("DocClassCode")
								+ "','"
								+ json.getString("DocClassName")
								+ "','"
								+ json.getString("DocCharacter")
								+ "','"
								+ json.getString("DocCharacterName")
								+ "','"
								+ json.getString("DocType")
								+ "','"
								+ json.getString("DocTitle")
								+ "','"
								+ json.getString("CompanyName")
								+ "','"
								+ json.getString("CompanyCode")
								+ "','"
								+ json.getString("UrgencyGrade")
								+ "','"
								+ json.getString("UrgencyGradeName")
								+ "','"
								+ json.getString("SecurityLevel")
								+ "','"
								+ json.getString("SecurityLevelName")
								+ "','"
								+ json.getString("KeyWord")
								+ "','"
								+ json.getString("MainTo")
								+ "','"
								+ json.getString("CoypTo")
								+ "','"
								+ json.getString("BackContent")
								+ "','"
								+ json.getString("Creater")
								+ "','"
								+ json.getString("CreaterId")
								+ "','"
								+ json.getString("CreaterUnitName")
								+ "','"
								+ json.getString("CreaterUnitCode")
								+ "','"
								+ json.getString("CreateDate")
								+ "','"
								+ json.getString("CreaterTelNo")
								+ "','"
								+ json.getString("QFAccount")
								+ "','"
								+ json.getString("QFName")
								+ "','"
								+ json.getString("QFDate")
								+ "','"
								+ json.getString("ReadCompanyCode")
								+ "','"
								+ json.getString("ReadSubUnitCode")
								+ "','"
								+ json.getString("SendCompanyCode")
								+ "','"
								+ json.getString("SendCompanyName")
								+ "','"
								+ json.getString("SendPersonId")
								+ "','"
								+ json.getString("SendPerson")
								+ "','"
								+ json.getString("SendUnitName")
								+ "','"
								+ json.getString("SendUnitCode")
								+ "','"
								+ json.getString("PrintSendDate")
								+ "','"
								+ json.getString("FileInfo")
								+ "','"
								+ json.getString("FileCount")
								+ "','"
								+ json.getString("VerIndex")
								+ "','"
								+ json.getString("ProcedureFeedBack")
								+ "','"
								+ json.getString("AuthCode")
								+ "','"
								+ json.getString("OperatorState") + "')");

				// 权限重构
			// addFormmodeRight("1", 9,"uf_cosgw");
			}
			result.put("STATUS", 0);
			result.put("MSG", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("接收的公文基础信息出现异常=====>" + e.getMessage());
			result.put("STATUS", 1);
			result.put("MSG", "接收的公文基础信息出现异常：" + e.getMessage());
		}
		rs = null;

		return JSONObject.fromObject(result).toString();
	}

	/**
	 * 获取公文附件
	 * 
	 * @param message 公文附件字符串（json格式）
	 * @return
	 */
	public String toCosGwFj(String message) {
		JSONObject json = JSONObject.fromObject(message);
		RecordSet rs = new RecordSet();
		new BaseBean().writeLog("接收的公文附件=====>" + json.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		String contentId = json.getString("ContentId");//文件ID
		String docInfoId = json.getString("DocInfoId");//公文ID
		String fileName = json.getString("FileName");//文件名
		String fileContent = json.getString("FileContent");//文件内容
		String fileSuffix = json.getString("FileSuffix");//文件格式
		String isMain = json.getString("IsMain");//是否正文
		String fileLength = json.getString("FileLength");//文件大小
		String authCode = json.getString("AuthCode");//授权码
		String operatorState = json.getString("OperatorState");//操作类型

		File txt=null;
		OutputStream os=null;
		byte[] bytes= null;
		try {
			//接收字符串写入文件  文件名以附件id-文件名表示
			txt=new File("/home/weaver/ecology/gwtemp/"+contentId+"-"+fileName);
			if(!txt.exists()){  
				txt.createNewFile();  
			}  
			bytes=fileContent.getBytes();
			os=new FileOutputStream(txt);
			os.write(bytes,0,bytes.length);
			os.flush();

			//判断数据库是否有值，有就更新，没有就插入
			rs.execute("select * from uf_cosgwfj where ContentId = '" + contentId + "'");
			if (rs.next()) {
				new RecordSet().execute("update uf_cosgwfj set DocInfoId='"+docInfoId+"',FileName='"+fileName+"',FileContent='/gwtemp/"+contentId+"-"+fileName+"',FileSuffix='"+fileSuffix+"',IsMain='"+isMain+"',FileLength='"+fileLength+"',AuthCode='"+authCode+"',OperatorState='"+operatorState+"' where ContentId = '" + contentId + "'");
			} else {
				new RecordSet().execute("insert into uf_cosgwfj(ContentId,DocInfoId,FileName,FileContent,FileSuffix,IsMain,FileLength,AuthCode,OperatorState) " +
						"values('"+contentId+"','"+docInfoId+"','"+fileName+"','/gwtemp/"+contentId+"-"+fileName+"','"+fileSuffix+"','"+isMain+"','"+fileLength+"','"+authCode+"','"+operatorState+"')");

				// 权限重构
				// addFormmodeRight("1", 9,"uf_cosgwfj");
			}
			result.put("STATUS", 0);
			result.put("MSG", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("接收的公文附件出现异常=====>" + e.getMessage());
			result.put("STATUS", 1);
			result.put("MSG", "接收的公文附件出现异常：" + e.getMessage());
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
					new BaseBean().writeLog("关闭输出流时出现异常");
				} finally {
					os = null;
				}
			}
			bytes = null;
			txt = null;
		}

		rs = null;

		return JSONObject.fromObject(result).toString();
	}

	// 权限重构
	public static void addFormmodeRight(String creatorid, int modeid,
			String formtable) {

		RecordSet rs = new RecordSet();
		int billid = 0;
		String sql = "select max(id) as maxid from " + formtable;
		rs.execute(sql);
		if (rs.next()) {
			billid = rs.getInt("maxid");
		}

		String modedatacreatedate = TimeUtil.getCurrentDateString();
		String modedatacreatetime = TimeUtil.getOnlyCurrentTimeString();

		sql = "update " + formtable + " set formmodeid=" + modeid
				+ ",modedatacreatertype=0,modedatacreater=" + creatorid
				+ ",modedatacreatedate='" + modedatacreatedate
				+ "',modedatacreatetime='" + modedatacreatetime + "' where id="
				+ billid;

		rs.execute(sql);

		// 构建数据权限
		ModeRightInfo modeRightInfo = new ModeRightInfo();
		modeRightInfo.setNewRight(true);
		modeRightInfo.editModeDataShare(Util.getIntValue(creatorid), modeid,
				billid);

	}

}
