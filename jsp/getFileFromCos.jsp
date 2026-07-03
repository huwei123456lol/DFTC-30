<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.hrm.User" %>
<%@ page import="com.api.doc.detail.util.DocDownloadCheckUtil" %>
<%@ page import="weaver.integration.util.SessionUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%
    String ipAddress = request.getRemoteAddr();
	User sysuser = new User(1);
    System.out.println("COS FILEDOWNLOAD IP ===>  " + ipAddress);

    String fileId = Util.null2String(request.getParameter("fileId"));
    String resultObj  = DocDownloadCheckUtil.EncodeFileid(fileId,sysuser);
    out.write(resultObj);

    if(!resultObj.equals("")){
        String site = "/weaver/weaver.file.FileDownload?fileid=" + resultObj;
        SessionUtil.createSession("1", request, response);
        SessionUtil.setCookie(request,response);
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
    }

%>