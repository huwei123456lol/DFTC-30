package weaver.trq.servlet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.docs.docs.DocImageManager;
import weaver.docs.docs.DocManager;
import weaver.docs.docs.ImageFileIdUpdate;
import weaver.docs.networkdisk.tools.ImageFileUtil;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.trq.webservice.rlghsjy.ProjectFileSoapProxy;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 获取项目文件（用于项目踏勘设计委托工作流程的ajax请求）的Servlet
 * 
 * @author Alex.Du
 * 
 */
public class GetProjectFileServlet extends HttpServlet {
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new BaseBean().writeLog("开始执行获取项目文件的Servlet");
		// 获取项目ID
		String xmId = request.getParameter("xid");
		// 获取requestId
		String requestId = request.getParameter("rid");

		// 通过项目ID查找项目编号
		RecordSet rs = new RecordSet();
		rs.execute("select xmbh from uf_xm where id =" + xmId);

		ProjectFileSoapProxy proxy = new ProjectFileSoapProxy();

		StringBuffer fileIDBuffer = new StringBuffer();// 用于拼接所有的文件ID
		try {

			if (rs.next()) {
				// 通过查找到的项目编号，调用接口，获取该项目的对应文件地址，并返回地址
				String result = proxy.getFilesByProjectCode(rs
						.getString("xmbh"));

				JSONObject jsonObject = new JSONObject(result);
				JSONArray projectFileArray = jsonObject
						.getJSONArray("ProjectFiles");
				// 循环文件列表，将每个文件上传至OA系统，将附件ID更新到sjyfj字段
				for (int i = 0; i < projectFileArray.length(); i++) {
					// 先通过文件的URL下载到硬盘
					URL url = null;
					DataInputStream dataInputStream = null;
					FileOutputStream fileOutputStream = null;
					ByteArrayOutputStream output = null;
					byte[] buffer = null;
					try {
						
						new BaseBean().writeLog("转换之前的文件地址为："+projectFileArray.getJSONObject(i)
								.getString("FilePath"));
						
						String base64FilePath = new String(Base64.decodeBase64(projectFileArray.getJSONObject(i)
								.getString("FilePath").getBytes()));
						
						new BaseBean().writeLog("Base64转换之后的文件地址为："+base64FilePath);
						
						//将文件的网络地址的最后的文件名部分进行encode转码（utf-8），然后再讲转码后的+号转换成%20，否则无法读到这个网络资源
						
						url = new URL(base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
						
						//url = new URL(projectFileArray.getJSONObject(i)
						//		.getString("FilePath").substring(0,projectFileArray.getJSONObject(i)
						//		.getString("FilePath").lastIndexOf("/")+1)+URLEncoder.encode(projectFileArray.getJSONObject(i).getString(
						//				"FileName"),"utf-8").replaceAll("\\+", "%20"));
						
						new BaseBean().writeLog("Base64转换并处理之后的文件地址为："+base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
						
						dataInputStream = new DataInputStream(url.openStream());

						fileOutputStream = new FileOutputStream(new File("D:"
								+ File.separator
								+ "WEAVER"
								+ File.separator
								+ "ecology"
								+ File.separator
								+ "tempfile"
								+ File.separator
								+ projectFileArray.getJSONObject(i).getString(
										"FileName")));
						output = new ByteArrayOutputStream();

						buffer = new byte[1024];
						int length = 0;

						while ((length = dataInputStream.read(buffer)) > 0) {
							output.write(buffer, 0, length);
						}
						fileOutputStream.write(output.toByteArray());
					} catch (MalformedURLException e) {
						e.printStackTrace();
						new BaseBean().writeLog("获取项目文件出现异常1:"+e.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
						new BaseBean().writeLog("获取项目文件出现异常2:"+e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						new BaseBean().writeLog("获取项目文件出现异常3:"+e.getMessage());
					} catch (Throwable e) {
						e.printStackTrace();
						new BaseBean().writeLog("获取项目文件出现异常4:"+e.getMessage());
					} finally {
						if (dataInputStream != null) {
							dataInputStream.close();
							dataInputStream = null;
						}
						if (output != null) {
							output.close();
							output = null;
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
							fileOutputStream = null;
						}
					}

					// 创建imagefile
					File file = new File("D:"
							+ File.separator
							+ "WEAVER"
							+ File.separator
							+ "ecology"
							+ File.separator
							+ "tempfile"
							+ File.separator
							+ projectFileArray.getJSONObject(i).getString(
									"FileName"));
					// 生成文件ID
					int imagefileid = new ImageFileIdUpdate()
							.getImageFileNewId();
					new BaseBean().writeLog("生成的文件id为：" + imagefileid);
					// 根据文件地址、文件ID、文件名称、文件大小在ecology文件系统中创建文件
					int createstatus = ImageFileUtil.createImageFile(
							"D:"
									+ File.separator
									+ "WEAVER"
									+ File.separator
									+ "ecology"
									+ File.separator
									+ "tempfile"
									+ File.separator
									+ projectFileArray.getJSONObject(i)
											.getString("FileName"),
							imagefileid, file.getName(), new FileInputStream(
									file).available());
					new BaseBean().writeLog("创建图片结果为：" + createstatus);
					
					// 创建docdetail
					int docid = createDocDetail(5, file.getName(), 1);
					// 创建docimagefile
					createDocImageFile(docid, imagefileid, file.getName());
					// 创建文档共享
					createDocShare(5, 1, docid);

					if (file.exists()) {
						file.delete();
					}

					if (fileIDBuffer.length() > 0) {
						fileIDBuffer.append(",");
					}
					fileIDBuffer.append(docid);
				}

				// 将拼接好的文件ID写入当前流程的sjyfj字段
				rs.execute("update formtable_main_55 set sjyfj='"
						+ fileIDBuffer.toString() + "' where requestid='"
						+ requestId + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("GetProjectFileServlet执行出现异常:"
					+ e.getMessage());
		} finally {
			rs = null;
			proxy = null;
		}
		new BaseBean().writeLog("执行获取项目文件的Servlet完毕");
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
	

	public static void main(String[] args) {
		String str = "aHR0cDovLzE3Mi4xNi4yLjE5L1BSSU5UL2RhdGFwYXRoLy80LzI2LzUyNjkyL82l1LrIvMb4udy1wMqpuaTNvC3IvCAtMTkxMzA4X1QtQS0ucGRm";
		byte[] b = Base64.decodeBase64(str.getBytes());
		
		try {
			String base64FilePath = new String(b,"GBK");
			System.out.println(base64FilePath);
			
			System.out.println("path1:"+base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1));
			
			System.out.println("path2:"+base64FilePath.substring(base64FilePath.lastIndexOf("/")+1));
			
			System.out.println("path3:"+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
			
			System.out.println("Base64转换并处理之后的文件地址为："+base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
