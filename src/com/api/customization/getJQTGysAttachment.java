package com.api.customization;

import com.alibaba.fastjson.JSONObject;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.rsa.security.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Hanjun
 * @date 2023/7/8
 * @apiNote
 */
@Path("/getJQTGysInfo")
public class getJQTGysAttachment {
	@GET
	@Path("/getfile")
	public Response getfile(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
		String docid = Util.null2String(request.getParameter("docid"));
		String ipAddr = getRemoteAddress(request);
		File f = null;
		String fName = null;
		DocServiceImpl docService = new DocServiceImpl();
		DocInfo docInfo = null;
		StreamingOutput fileStream = null;
		try {
			docInfo = docService.getDocByUser(Integer.parseInt(docid), new User(1), "");
			DocAttachment[] docAttachment = docInfo.getAttachments();
			fName = docAttachment[0].getFilename();
			String filerealpath = docAttachment[0].getFilerealpath();
			fileStream = output -> {
				if (docAttachment[0].getIszip() == 1) {
					ZipFile zipFile = new ZipFile(filerealpath);
					Enumeration<? extends ZipEntry> e = zipFile.entries();
					if (e.hasMoreElements()) {
						try {
							ZipEntry ze2 = e.nextElement();
							ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
							BufferedInputStream bi = new BufferedInputStream(zipFile.getInputStream(ze2));
							byte[] readContent = new byte[1024];
							int readCount = bi.read(readContent);
							while (readCount != -1) {
								bos.write(readContent, 0, readCount);
								readCount = bi.read(readContent);
							}
							byte[] data = bos.toByteArray();
							output.write(data);
							output.flush();
							bos.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else {

					byte[] data = Files.readAllBytes(Paths.get(docAttachment[0].getFilerealpath()));
					output.write(data);
					output.flush();
				}
			};

		} catch (Exception e) {
			e.printStackTrace();
		}
		//Ìí¼ÓIPÏÞÖÆ
		if(ipAddr.contains("10.4.9.") ||ipAddr.contains("10.4.10.") ){
			return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
					.header("content-disposition",
							"attachment; filename = " + fName)
					.build();
		}else{
			return Response.serverError().build();
		}

	}

	@GET
	@Path("/getfileInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getfileInfo(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
		JSONObject result = new JSONObject();
		String ipAddr = getRemoteAddress(request);
		result.put("ipAddr",ipAddr);
		String docid = Util.null2String(request.getParameter("docid"));
		File f = null;
		String fName = null;
		DocServiceImpl docService = new DocServiceImpl();
		DocInfo docInfo = null;
		try {
			docInfo = docService.getDocByUser(Integer.parseInt(docid), new User(1), "");
			DocAttachment[] docAttachment = docInfo.getAttachments();
			f = new File(docAttachment[0].getFilerealpath());
			docAttachment[0].getIszip();
			fName = docAttachment[0].getFilename();

			result.put("fname", fName);
			result.put("frealpath", docAttachment[0].getFilerealpath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toJSONString(result);
	}

	public String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		String unknown = "unknown";
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
