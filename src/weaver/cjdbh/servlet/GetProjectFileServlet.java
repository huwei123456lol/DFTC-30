package weaver.cjdbh.servlet;

import com.ibm.icu.text.SimpleDateFormat;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ��ȡ��Ŀ�ļ���������Ŀ̤�����ί�й������̵�ajax���󣩵�Servlet
 * 
 * @author Alex.Du
 * 
 */
public class GetProjectFileServlet extends HttpServlet {
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		new BaseBean().writeLog("��ʼִ�л�ȡ��Ŀ�ļ���Servlet");
		// ��ȡ��ĿID
		String xmId = request.getParameter("xid");
		// ��ȡrequestId
		String requestId = request.getParameter("rid");

		// ͨ����ĿID������Ŀ���
		RecordSet rs = new RecordSet();
		rs.execute("select xmbh from uf_xm where id =" + xmId);

		ProjectFileSoapProxy proxy = new ProjectFileSoapProxy();

		StringBuffer fileIDBuffer = new StringBuffer();// ����ƴ�����е��ļ�ID
		try {

			if (rs.next()) {
				// ͨ�����ҵ�����Ŀ��ţ����ýӿڣ���ȡ����Ŀ�Ķ�Ӧ�ļ���ַ�������ص�ַ
				String result = proxy.getFilesByProjectCode(rs
						.getString("xmbh"));

				JSONObject jsonObject = new JSONObject(result);
				JSONArray projectFileArray = jsonObject
						.getJSONArray("ProjectFiles");
				// ѭ���ļ��б���ÿ���ļ��ϴ���OAϵͳ��������ID���µ�sjyfj�ֶ�
				for (int i = 0; i < projectFileArray.length(); i++) {
					// ��ͨ���ļ���URL���ص�Ӳ��
					URL url = null;
					DataInputStream dataInputStream = null;
					FileOutputStream fileOutputStream = null;
					ByteArrayOutputStream output = null;
					byte[] buffer = null;
					try {
						
						new BaseBean().writeLog("ת��֮ǰ���ļ���ַΪ��"+projectFileArray.getJSONObject(i)
								.getString("FilePath"));
						
						String base64FilePath = new String(Base64.decodeBase64(projectFileArray.getJSONObject(i)
								.getString("FilePath").getBytes()));
						
						new BaseBean().writeLog("Base64ת��֮����ļ���ַΪ��"+base64FilePath);
						
						//���ļ��������ַ�������ļ������ֽ���encodeת�루utf-8����Ȼ���ٽ�ת����+��ת����%20�������޷��������������Դ
						
						url = new URL(base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
						
						//url = new URL(projectFileArray.getJSONObject(i)
						//		.getString("FilePath").substring(0,projectFileArray.getJSONObject(i)
						//		.getString("FilePath").lastIndexOf("/")+1)+URLEncoder.encode(projectFileArray.getJSONObject(i).getString(
						//				"FileName"),"utf-8").replaceAll("\\+", "%20"));
						
						new BaseBean().writeLog("Base64ת��������֮����ļ���ַΪ��"+base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
						
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
						new BaseBean().writeLog("��ȡ��Ŀ�ļ������쳣1:"+e.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
						new BaseBean().writeLog("��ȡ��Ŀ�ļ������쳣2:"+e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						new BaseBean().writeLog("��ȡ��Ŀ�ļ������쳣3:"+e.getMessage());
					} catch (Throwable e) {
						e.printStackTrace();
						new BaseBean().writeLog("��ȡ��Ŀ�ļ������쳣4:"+e.getMessage());
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

					// ����imagefile
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
					// �����ļ�ID
					int imagefileid = new ImageFileIdUpdate()
							.getImageFileNewId();
					new BaseBean().writeLog("���ɵ��ļ�idΪ��" + imagefileid);
					// �����ļ���ַ���ļ�ID���ļ����ơ��ļ���С��ecology�ļ�ϵͳ�д����ļ�
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
					new BaseBean().writeLog("����ͼƬ���Ϊ��" + createstatus);
					
					// ����docdetail
					int docid = createDocDetail(5, file.getName(), 1);
					// ����docimagefile
					createDocImageFile(docid, imagefileid, file.getName());
					// �����ĵ�����
					createDocShare(5, 1, docid);

					if (file.exists()) {
						file.delete();
					}

					if (fileIDBuffer.length() > 0) {
						fileIDBuffer.append(",");
					}
					fileIDBuffer.append(docid);
				}

				// ��ƴ�Ӻõ��ļ�IDд�뵱ǰ���̵�sjyfj�ֶ�
				rs.execute("update formtable_main_55 set sjyfj='"
						+ fileIDBuffer.toString() + "' where requestid='"
						+ requestId + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			new BaseBean().writeLog("GetProjectFileServletִ�г����쳣:"
					+ e.getMessage());
		} finally {
			rs = null;
			proxy = null;
		}
		new BaseBean().writeLog("ִ�л�ȡ��Ŀ�ļ���Servlet���");
	}
	
	
	/**
	 * �����ĵ� docdetail��
	 * @param seccategory  Ŀ¼
	 * @param filename �ļ���
	 * @param docOwner ������
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
		Map<String,String> rsp = isOpenApproveWfByDocSeccategoryId(seccategory);	//��ȡĿ¼��Ϣ
		dm.setId(docid);
		dm.setMaincategory(0);
		dm.setSubcategory(0);
		dm.setSeccategory(seccategory);
		dm.setDoclangurage(7);	//
		dm.setDocapprovable("");	//Ĭ�ϲ�����	
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
		dm.setAccessorycount(1);	//����������Ĭ��1
		dm.setReplaydoccount(0);
		dm.setDocCreaterType("1");
		dm.setDocType(1);
		dm.setCanCopy("1");
		dm.setCanRemind("1");
		dm.setOrderable(rsp.get("orderable"));
		dm.setDocextendname("html");	//Ĭ��html�ĵ�
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
			bb.writeLog("===���docdetail���");
		}catch(Exception e){
			bb.writeLog("===���docdetail�쳣",e);
		}
		return docid;
	}

	
	/**
	 * ����docimagefile��
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
        bb.writeLog("===����docimagefile���");
	}
	
	/**
	 * �����ĵ��Ĺ���
	 * @param seccategoryId Ŀ¼id
	 * @param ownerId �ĵ�������
	 * @param docid �ĵ�id
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
			bb.writeLog("======createDocShare�쳣",e);
		}
		RecordSet rs = new RecordSet();
        rs.executeProc("Share_forDoc",""+docid);
        dm.setUsertype("1");
        dm.setAboutCreaterShare(seccategoryId+"");
	}
	
	/**
     * �õ��ĵ�����չ��
     * 
     * @param file �ĵ�ȫ��
     * @return �ĵ�����չ��
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
			bb.writeLog("======��ѯĿ¼��Ϣ��ֵ");
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
			
			System.out.println("Base64ת��������֮����ļ���ַΪ��"+base64FilePath.substring(0,base64FilePath.lastIndexOf("/")+1)+URLEncoder.encode(base64FilePath.substring(base64FilePath.lastIndexOf("/")+1),"utf-8").replaceAll("\\+", "%20"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
