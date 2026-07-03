package weaver.dfqcgsjszx.quartz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.util.gw.service_client.ISendMessageServiceProxy;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Base64;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 获取公文信息 公文文件信息 ，并回传状态
 * @author peixuan
 *	Date 2020-07-28
 */
public class GetGwMsgQuartz extends BaseCronJob{

	public void execute() {
		toCosGwMessage("1X00000000");
	}
	/**
	 * 获取公文基础信息
	 * 
	 * @param message 
	 * @return
	 */
	public void toCosGwMessage(String message) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		//构建参数，调用接口
        //构建接口头部参数
        JSONObject paramHead = new JSONObject();
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_GW_003");
        paramHead.put("tradeDescription", "获取公文基础信息数据");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");

        String result = null;
        try {
            result = new ISendMessageServiceProxy().sendMessage(paramHead.toString(), message);
        } catch (Exception e) {
            e.printStackTrace();
            log.writeLog("[GetGwMsgQuartz]获取公文基础信息数据出现异常：" + e.getMessage());
        }
		
		JSONObject json = JSONObject.fromObject(result);
		log.writeLog("[GetGwMsgQuartz]接收的公文基础信息==>" + json.toString());
		if(!json.getString("result").equals("true")){log.writeLog("[GetGwMsgQuartz]接收的公文基础信息失败");return;}

		JSONArray arrayjson = JSONArray.fromObject(json.getString("message"));

		if(arrayjson.size()>0){
			for(int i = 0;i<arrayjson.size();i++){
				JSONObject jsonString = arrayjson.getJSONObject(i);
				//操作状态
				String operatorState = jsonString.getString("OperatorState");
				try {
					rs.execute("select * from uf_cosgw where docinfoid = '"
							+ jsonString.getString("DocInfoId") + "'");
					if (rs.next()) {
						String sql = "update uf_cosgw set DocNoKey = '"
							+ jsonString.getString("DocNoKey") + "',DocDateYear = '"
							+ jsonString.getString("DocDateYear") + "',DispatchNo = '"
							+ jsonString.getString("DispatchNo") + "',DocNo = '"
							+ jsonString.getString("DocNo") + "',DocNoCode = '"
							+ jsonString.getString("DocNoCode") + "',DocClassCode = '"
							+ jsonString.getString("DocClassCode") + "',DocClassName = '"
							+ jsonString.getString("DocClassName") + "',DocCharacter = '"
							+ jsonString.getString("DocCharacter")
							+ "',DocCharacterName = '"
							+ jsonString.getString("DocCharacterName") + "',DocType = '"
							+ jsonString.getString("DocType") + "',DocTitle = '"
							+ jsonString.getString("DocTitle") + "',CompanyName = '"
							+ jsonString.getString("CompanyName") + "',CompanyCode = '"
							+ jsonString.getString("CompanyCode") + "',UrgencyGrade = '"
							+ jsonString.getString("Urgencygrade")
							+ "',UrgencyGradeName = '"
							+ jsonString.getString("UrgencyGradeName")
							+ "',SecurityLevel = '"
							+ jsonString.getString("SecurityLevel")
							+ "',SecurityLevelName = '"
							+ jsonString.getString("SecurityLevelName") + "',KeyWord = '"
							+ jsonString.getString("KeyWord") + "',MainTo = '"
							+ jsonString.getString("MainTo") + "',CoypTo = '"
							+ jsonString.getString("CoypTo") + "',BackContent = '"
							+ jsonString.getString("BackContent") + "',Creater = '"
							+ jsonString.getString("Creater") + "',CreaterId = '"
							+ jsonString.getString("CreaterId") + "',CreaterUnitName = '"
							+ jsonString.getString("CreaterUnitName")
							+ "',CreaterUnitCode = '"
							+ jsonString.getString("CreaterUnitCode")
							+ "',CreateDate = '" + jsonString.getString("CreateDate")
							+ "',CreaterTelNo = '" + jsonString.getString("CreaterTelNo")
							+ "',QFAccount = '" + jsonString.getString("QFAccount")
							+ "',QFName = '" + jsonString.getString("QFName")
							+ "',QFDate = '" + jsonString.getString("QFDate")
							+ "',ReadCompanyCode = '"
							+ jsonString.getString("ReadCompanyCode")
							+ "',ReadSubUnitCode = '"
							+ jsonString.getString("ReadSubUnitCode")
							+ "',SendCompanyCode = '"
							+ jsonString.getString("SendCompanyCode")
							+ "',SendCompanyName = '"
							+ jsonString.getString("SendCompanyName")
							+ "',SendPersonId = '" + jsonString.getString("SendPersonId")
							+ "',SendPerson = '" + jsonString.getString("SendPerson")
							+ "',SendUnitName = '" + jsonString.getString("SendUnitName")
							+ "',SendUnitCode = '" + jsonString.getString("SendUnitCode")
							+ "',PrintSendDate = '"
							+ jsonString.getString("PrintSendDate") + "',FileCount = '"
							+ jsonString.getString("FileCount") + "',VerIndex = '"
							+ jsonString.getString("VerIndex")
							+ "',AuthCode = '" + jsonString.getString("AuthCode")
							+ "',OperatorState = '" + operatorState+ "' where DocInfoId = '" + jsonString.getString("DocInfoId")+ "'";
						new RecordSet().execute(sql);
						log.writeLog("[GetGwMsgQuartz]公文基础数据更新==>"+sql);
					} else {
						String sql = "insert into uf_cosgw(DocInfoId,DocNoKey,DocDateYear,DispatchNo,DocNo,DocNoCode,DocClassCode,DocClassName,DocCharacter,DocCharacterName,DocType,DocTitle,CompanyName,CompanyCode,UrgencyGrade,UrgencyGradeName,SecurityLevel,SecurityLevelName,KeyWord,MainTo,CoypTo,BackContent,Creater,CreaterId,CreaterUnitName,CreaterUnitCode,CreateDate,CreaterTelNo,QFAccount,QFName,QFDate,ReadCompanyCode,ReadSubUnitCode,SendCompanyCode,SendCompanyName,SendPersonId,SendPerson,SendUnitName,SendUnitCode,PrintSendDate,FileCount,VerIndex,AuthCode,OperatorState) "
							+ "values('"
							+ jsonString.getString("DocInfoId")
							+ "','"
							+ jsonString.getString("DocNoKey")
							+ "','"
							+ jsonString.getString("DocDateYear")
							+ "','"
							+ jsonString.getString("DispatchNo")
							+ "','"
							+ jsonString.getString("DocNo")
							+ "','"
							+ jsonString.getString("DocNoCode")
							+ "','"
							+ jsonString.getString("DocClassCode")
							+ "','"
							+ jsonString.getString("DocClassName")
							+ "','"
							+ jsonString.getString("DocCharacter")
							+ "','"
							+ jsonString.getString("DocCharacterName")
							+ "','"
							+ jsonString.getString("DocType")
							+ "','"
							+ jsonString.getString("DocTitle")
							+ "','"
							+ jsonString.getString("CompanyName")
							+ "','"
							+ jsonString.getString("CompanyCode")
							+ "','"
							+ jsonString.getString("Urgencygrade")
							+ "','"
							+ jsonString.getString("UrgencyGradeName")
							+ "','"
							+ jsonString.getString("SecurityLevel")
							+ "','"
							+ jsonString.getString("SecurityLevelName")
							+ "','"
							+ jsonString.getString("KeyWord")
							+ "','"
							+ jsonString.getString("MainTo")
							+ "','"
							+ jsonString.getString("CoypTo")
							+ "','"
							+ jsonString.getString("BackContent")
							+ "','"
							+ jsonString.getString("Creater")
							+ "','"
							+ jsonString.getString("CreaterId")
							+ "','"
							+ jsonString.getString("CreaterUnitName")
							+ "','"
							+ jsonString.getString("CreaterUnitCode")
							+ "','"
							+ jsonString.getString("CreateDate")
							+ "','"
							+ jsonString.getString("CreaterTelNo")
							+ "','"
							+ jsonString.getString("QFAccount")
							+ "','"
							+ jsonString.getString("QFName")
							+ "','"
							+ jsonString.getString("QFDate")
							+ "','"
							+ jsonString.getString("ReadCompanyCode")
							+ "','"
							+ jsonString.getString("ReadSubUnitCode")
							+ "','"
							+ jsonString.getString("SendCompanyCode")
							+ "','"
							+ jsonString.getString("SendCompanyName")
							+ "','"
							+ jsonString.getString("SendPersonId")
							+ "','"
							+ jsonString.getString("SendPerson")
							+ "','"
							+ jsonString.getString("SendUnitName")
							+ "','"
							+ jsonString.getString("SendUnitCode")
							+ "','"
							+ jsonString.getString("PrintSendDate")
							+ "','"
							+ jsonString.getString("FileCount")
							+ "','"
							+ jsonString.getString("VerIndex")
							+ "','"
							+ jsonString.getString("AuthCode")
							+ "','"
							+ operatorState + "')";
						new RecordSet().execute(sql);
						log.writeLog("[GetGwMsgQuartz]公文基础数据插入==>"+sql);

						// 权限重构
						 addFormmodeRight("1", 76501,"uf_cosgw");
					}
					
					//当状态为1或者4时，调用获取公文附件方法
					if(operatorState.equals("1")||operatorState.equals("4")){
						//获取公文附件
						Map<String,String> map = toCosGwFj(jsonString.getString("DocInfoId"),jsonString.getString("AuthCode"));
						String sql = "update uf_cosgw set zw = '" + map.get("zwid") + "',fj = '"+map.get("fjid")+"' where DocInfoId = '" + jsonString.getString("DocInfoId")+ "'";
						new RecordSet().execute(sql);
						log.writeLog("[GetGwMsgQuartz]更新公文附件sql==>"+sql);
					}
					
					JSONObject param = new JSONObject();
					param.put("clientCode", "DFTC_COS");
					param.put("reqSerialNo", UUID.randomUUID().toString());
					param.put("tradeCode", "DFG_GW_002");
					param.put("tradeDescription", "公文数据状态回传");
					param.put("tradeTime", TimeUtil.getCurrentTimeString());
					param.put("version", "1.0");
			        
			        //构建接口内容参数
			        String paramBody = "{'statelist': [{'docInfoId': '"+jsonString.getString("DocInfoId")+"','ReadCompanyCode': '"+message+"','State': true,'OperatorState':'1'}]}";
			        
			        String msg =  new ISendMessageServiceProxy().sendMessage(param.toString(), paramBody);
					log.writeLog("[GetGwMsgQuartz]回传公文数据状态==>"+msg);
					
					
				} catch (Exception e) {
					e.printStackTrace();
					log.writeLog("[GetGwMsgQuartz]接收的公文基础信息出现异常==>" + e.getMessage());
				}
			}
		}
		rs = null;

	}

	/**
	 * 获取公文附件
	 * 
	 * @param gwid 公文ID
	 * @param sqm  授权码
	 * @return
	 */
	public Map<String,String> toCosGwFj(String gwid,String sqm) {
		BaseBean baseBean = new BaseBean();
		JSONObject paramHead = new JSONObject();
        paramHead.put("clientCode", "DFTC_COS");
        paramHead.put("reqSerialNo", UUID.randomUUID().toString());
        paramHead.put("tradeCode", "DFG_GW_004");
        paramHead.put("tradeDescription", "获取公文文件数据");
        paramHead.put("tradeTime", TimeUtil.getCurrentTimeString());
        paramHead.put("version", "1.0");
        
        //构建接口内容参数
        String paramBody = "{'idlist': [{'DocInfoId': '"+gwid+"','AuthCode': '"+sqm+"'}]}";
        
        String result = null;
        try {
            result = new ISendMessageServiceProxy().sendMessage(paramHead.toString(), paramBody);
        } catch (Exception e) {
            e.printStackTrace();
            new BaseBean().writeLog("[GetGwMsgQuartz]获取公文文件数据出现异常：" + e.getMessage());
        }
		
        JSONObject json = JSONObject.fromObject(result);
		baseBean.writeLog("[GetGwMsgQuartz]接收的公文附件=====>" + json.toString());
        Map<String, String> map = new HashMap<String, String>();
		if(!json.getString("result").equals("true")){
			baseBean.writeLog("[GetGwMsgQuartz]接收的公文附件失败");
			map.put("zwid", "");
		    map.put("fjid", "");
			return map;
		}
        JSONArray arrayjson = JSONArray.fromObject(json.getString("message"));
        String zwid = "";
        String fjid = "";
        if(arrayjson.size()>0){
        	for(int i = 0;i<arrayjson.size();i++){
        		JSONObject jsonString = arrayjson.getJSONObject(i);

        		String contentId = jsonString.getString("DocContentId");//文件ID
        		String docInfoId = jsonString.getString("DocInfoId");//公文ID
        		String fileName = jsonString.getString("FileName");//文件名
        		String fileSuffix = jsonString.getString("FileSuffix");//文件格式
        		String isMain = jsonString.getString("IsMain");//是否正文
        		String filePath = jsonString.getString("FilePath");//路径
        		String fileContent = jsonString.getString("FileContent");//文件内容

        		/*FTPClient ftpClient = null; // 创建一个客户端实例
        		OutputStream os = null;
        		File file = null;
        		try {
        			file = new File("/home/weaver/ecology/filesystem/gwtemp/" + contentId+"."+fileSuffix);
        			os = new FileOutputStream(file);
        			ftpClient = getFTPClient("10.3.251.12", "DFG_GW", "DFesb@0728", 21);
        			ftpClient.enterLocalPassiveMode();
        			ftpClient.changeWorkingDirectory("/var/ftp/DFG_GW/"+filePath);// 文件上传路径 /var/ftp/DFG_GW/
        			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        			ftpClient.retrieveFile(fileName, os); // 开始下载正文文件

        		} catch (Exception e) {
        			new BaseBean().writeLog("[GetGwMsgQuartz]uploadToFTPServer文件下载失败！");
        			e.printStackTrace();
        		} finally {
        			if (ftpClient.isConnected()) {
        				try {
        					ftpClient.disconnect();
        				} catch (IOException ioe) {
        					ioe.printStackTrace();
        				}
        			}
        			if (os != null) {
        				try {
        					os.close();
        				} catch (IOException e) {
        					e.printStackTrace();
        				} finally {
        					os = null;
        				}
        			}
        		}*/
        		
        		File file=null;
        		OutputStream os=null;
        		byte[] bytes= null;
        		try {
        			//接收字符串写入文件  文件名以附件id-文件名表示
        			file=new File("/home/weaver/ecology/filesystem/gwtemp/" + contentId + "."+fileSuffix);
        			if(!file.exists()){  
        				file.createNewFile();  
        			}  
        			bytes=new Base64().decode(fileContent.getBytes("UTF-8"));
        			os=new FileOutputStream(file);
        			os.write(bytes,0,bytes.length);
        			os.flush();

        		} catch (Exception e) {
        			e.printStackTrace();
        			new BaseBean().writeLog("[GetGwMsgQuartz]接收的公文附件出现异常==>" + e.getMessage());
        		}finally {
        			if (os != null) {
        				try {
        					os.close();
        				} catch (Exception e) {
        					e.printStackTrace();
        					new BaseBean().writeLog("[GetGwMsgQuartz]关闭输出流时出现异常");
        				} finally {
        					os = null;
        				}
        			}
        			bytes = null;
        		}
        		
        		// 创建imagefile
    			//File file = new File("/home/weaver/ecology/filesystem/gwtemp/" + contentId+"."+fileSuffix);
        		// 生成文件ID
        		int imagefileid = new ImageFileIdUpdate().getImageFileNewId();
        		// 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
        		int createstatus = ImageFileUtil.createImageFile("/home/weaver/ecology/filesystem/gwtemp/" + contentId+"."+fileSuffix, imagefileid, fileName, getFileSize(file));
        		if (createstatus == -1) {
        			new BaseBean().writeLog("[GetGwMsgQuartz]公文上传文件系统失败");
        		}else{
	        		// 创建docdetail
	        		int docid = createDocDetail(8543, fileName, 1);
	        		// 创建docimagefile
	        		createDocImageFile(docid, imagefileid, fileName);
	        		// 创建文档共享
	        		createDocShare(8543, 1, docid);
	        		new BaseBean().writeLog("[GetGwMsgQuartz]存储到ecology系统中的文件ID为：" + docid);
	        		//正文
	        		if(isMain.equals("1")){
	        			zwid += ","+docid;
	        		//附件
	        		}else if(isMain.equals("0")){
	        			fjid += ","+docid;
	        		}
	        		// 删除生成在硬盘中的word文件
	        		file.delete();
        		}
        	}
        	
        	if(zwid.length()>0){zwid = zwid.substring(1,zwid.length());}
        	if(fjid.length()>0){fjid = fjid.substring(1,fjid.length());}
        }
        map.put("zwid", zwid.equals("")?"-1":zwid);
    	map.put("fjid", fjid.equals("")?"-1":fjid);

        return map;
	}
	
	
	/**
	 * 创建文档的共享
	 * @param seccategoryId 目录id
	 * @param ownerId 文档所有者
	 * @param docid 文档id
	 */
	private void createDocShare(int seccategoryId,int ownerId,int docid){
		BaseBean bb = new BaseBean();
		bb.writeLog("======seccategoryId:"+seccategoryId+",ownerId:"+ownerId+",docid:"+docid);
		DocManager dm = new DocManager();
		dm.setUserid(ownerId);
		dm.setId(docid);
		dm.setDocCreaterType("1");
		dm.setSeccategory(seccategoryId);
		try {
			dm.AddShareInfo();
		} catch (Exception e) {
			bb.writeLog("======createDocShare异常",e);
		}
		RecordSet rs = new RecordSet();
        rs.executeProc("Share_forDoc",""+docid);
        dm.setUsertype("1");
        dm.setAboutCreaterShare(seccategoryId+"");
	}
	
	/**
	 * 创建docimagefile表
	 * @param docid
	 * @param imagefileid
	 * @param filename
	 */
	private void createDocImageFile(int docid,int imagefileid,String filename){
		DocImageManager imgManger = new DocImageManager();
		BaseBean bb = new BaseBean();
		imgManger.setDocid(docid);
        imgManger.setImagefileid(imagefileid);
        imgManger.setImagefilename(filename);
        imgManger.setIsextfile("1");
        String ext = getFileExt(filename);
        if (ext.equalsIgnoreCase("doc")) {
            imgManger.setDocfiletype("3");
        } else if (ext.equalsIgnoreCase("xls")) {
            imgManger.setDocfiletype("4");
        } else if (ext.equalsIgnoreCase("ppt")) {
            imgManger.setDocfiletype("5");
        } else if (ext.equalsIgnoreCase("wps")) {
            imgManger.setDocfiletype("6");
        } else if (ext.equalsIgnoreCase("docx")) {
            imgManger.setDocfiletype("7");
        } else if (ext.equalsIgnoreCase("xlsx")) {
            imgManger.setDocfiletype("8");
        } else if (ext.equalsIgnoreCase("pptx")) {
            imgManger.setDocfiletype("9");
        } else if (ext.equalsIgnoreCase("et")) {
            imgManger.setDocfiletype("10");
        } else {
            imgManger.setDocfiletype("2");
        }
        imgManger.AddDocImageInfo();
        bb.writeLog("===创建docimagefile完成");
	}
	
	
	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 */
	private int getFileSize(File file) {
		String filesize = "0";
		DecimalFormat df = new DecimalFormat("#.##");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			filesize = df.format((double) ((double) fis.available() / 1024));
		} catch (Exception e) {

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {

				}
			}
		}
		if (filesize != null && filesize.contains(".")) {
			filesize = filesize.split("\\.")[0];
		}

		return Integer.parseInt(filesize);
	}
	/**
     * 得到文档的扩展名
     * 
     * @param file 文档全名
     * @return 文档的扩展名
     */
    public String getFileExt(String file) {
        if (file == null || file.trim().equals("")) {
            return "";
        } else {
            int idx = file.lastIndexOf(".");
            if (idx == -1) {
                return "";
            } else {
                if (idx + 1 >= file.length()) {
                    return "";
                } else {
                    return file.substring(idx + 1);
                }
            }
        }
    }
    
    /**
	 * 创建文档 docdetail表
	 * @param seccategory  目录
	 * @param filename 文件名
	 * @param docOwner 创建人
	 * @return
	 */
	private int createDocDetail(int seccategory,String filename,int docOwner){
		DocManager dm = new DocManager();
		BaseBean bb = new BaseBean();
		int docid=0;
		try {
			docid = dm.getNextDocId(new RecordSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> rsp = isOpenApproveWfByDocSeccategoryId(seccategory);	//获取目录信息
		dm.setId(docid);
		dm.setMaincategory(0);
		dm.setSubcategory(0);
		dm.setSeccategory(seccategory);
		dm.setDoclangurage(7);	//
		dm.setDocapprovable("");	//默认不审批	
		dm.setDocreplyable(rsp.get("replyable"));
		dm.setIsreply("");
		dm.setReplydocid(0);
		dm.setDocsubject(filename);
		dm.setDocpublishtype("");
		dm.setItemid(0);
		dm.setItemmaincategoryid(0);
		dm.setHrmresid(0);
		dm.setCrmid(0);
		dm.setProjectid(0);
		dm.setFinanceid(0);
		dm.setDoccreaterid(docOwner);
		ResourceComInfo hrc = null;
		try {
			hrc = new ResourceComInfo();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		dm.setDocdepartmentid(Util.getIntValue(hrc.getDepartmentID("" + docOwner)));
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		String timeStr = new SimpleDateFormat("HH:mm:ss").format(date);
		dm.setDoccreatedate(dateStr);
		dm.setDoccreatetime(timeStr);
		dm.setDoclastmoduserid(docOwner);
		dm.setDoclastmoddate(dateStr);
		dm.setDoclastmodtime(timeStr);
		dm.setDocapproveuserid(0);
		dm.setDocapprovedate("");
		dm.setDocapprovetime("");
		dm.setDocarchiveuserid(docOwner);
		dm.setDocarchivedate("");
		dm.setDocarchivetime("");
		dm.setDocstatus("1");
		dm.setParentids(docid+"");
		dm.setAssetid(0);
		dm.setOwnerid(docOwner);
		dm.setKeyword("");
		dm.setAccessorycount(1);	//附件个数，默认1
		dm.setReplaydoccount(0);
		dm.setDocCreaterType("1");
		dm.setDocType(1);
		dm.setCanCopy("1");
		dm.setCanRemind("1");
		dm.setOrderable(rsp.get("orderable"));
		dm.setDocextendname("html");	//默认html文档
		dm.setDocCode("");
		dm.setDocEdition(-1);
		dm.setDocEditionId(-1);
		dm.setIsHistory(0);
		dm.setApproveType(0);
		dm.setMainDoc(docid);
		String readoptercanprint = rsp.get("readoptercanprint");
		dm.setReadOpterCanPrint("".equals(readoptercanprint)?0:Integer.valueOf(readoptercanprint));
		dm.setDocValidUserId(docOwner);
		dm.setDocValidDate(dateStr);
		dm.setDocValidTime(timeStr);
		dm.setInvalidationDate("");
		dm.setDocCreaterType("1");
		dm.setDocLastModUserType("1");
		dm.setDocApproveUserType("");
		dm.setDocValidUserType("");
		dm.setDocInvalUserType("");
		dm.setDocArchiveUserType("");
		dm.setDocCancelUserType("");
		dm.setDocPubUserType("");
		dm.setDocCancelUserType("");
		dm.setDocPubUserType("");
		dm.setDocReopenUserType("");
		dm.setOwnerType("1");
		dm.setDoccontent("");
		try{
			dm.AddDocInfo();
			bb.writeLog("===添加docdetail完成");
		}catch(Exception e){
			bb.writeLog("===添加docdetail异常",e);
		}
		return docid;
	}
	

	private Map<String,String> isOpenApproveWfByDocSeccategoryId(int seccategory){
		RecordSet rs = new RecordSet();
		BaseBean bb = new BaseBean();
		Map<String,String> rsp = new HashMap<String,String>();
		rs.execute("select isOpenApproveWf,replyable,orderable,readoptercanprint from docseccategory where id = "+seccategory);
		if(rs.next()){
			rsp.put("isOpenApproveWf", rs.getString("isOpenApproveWf"));
			rsp.put("replyable", rs.getString("replyable"));
			rsp.put("orderable", rs.getString("orderable"));
			rsp.put("readoptercanprint", rs.getString("readoptercanprint"));
			bb.writeLog("======查询目录信息有值");
			return rsp;
		}
		return rsp;
	}

	// 权限重构
	public void addFormmodeRight(String creatorid, int modeid,
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
	/**
	 * 链接FTP
	 * @param ftpHost
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param ftpPort
	 * @return
	 */
	public FTPClient getFTPClient(String ftpHost, String ftpUserName,
			String ftpPassword, int ftpPort) {

		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
			ftpClient.enterLocalActiveMode(); // 主动模式 主：FTP服务器也要配置成主动模式
			// ftpClient.enterLocalPassiveMode(); 被动模式
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器

			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				new BaseBean().writeLog("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				new BaseBean().writeLog("FTP连接成功。");
			}
		} catch (SocketException e) {
			new BaseBean().writeLog("FTP的IP地址可能错误，请正确配置。");
			e.printStackTrace();
		} catch (IOException e) {
			new BaseBean().writeLog("FTP的端口错误,请正确配置。");
			e.printStackTrace();
		}
		return ftpClient;

	}
	
	

}
