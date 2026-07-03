package com.api.customization;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Hanjun
 * @date 2021/12/24
 * @apiNote
 */


@Path("/MediaService")
class MediaService {


	@GET
	@Path("/downloadImg")
	@Consumes("image/*")
	@Produces("image/png")
	public Response.ResponseBuilder checkToken(String param) throws JSONException {
		JSONObject req = JSONObject.parseObject(param);
		JSONObject result = new JSONObject();
		new BaseBean().writeLog("[222222]" + param);
		String token = req.getString("token");
		String sign = req.getString("sign");
		new BaseBean().writeLog("[222222]" + token);
		new BaseBean().writeLog("[222222]" + sign);
		long timestamp = Long.parseLong(Util.null2String(req.getString("timestamp")));

		DocServiceImpl docService = new DocServiceImpl();
		DocInfo docInfo = null;
		try {
			docInfo = docService.getDocByUser(1,new User(1),"");
			DocAttachment[] docAttachment =  docInfo.getAttachments();
			docAttachment[0].getFilerealpath();

		} catch (Exception e) {
			e.printStackTrace();
		}


		String signStr = DigestUtils.shaHex("IAM" + timestamp);
		if(!sign.equals(signStr)){
			result.put("status","1");
			result.put("msg","签名异常");
		} else if (Math.abs(System.currentTimeMillis()/1000 - timestamp ) > 30){
			result.put("status","1");
			result.put("msg","已超时");
		}else{
			RecordSet rs = new RecordSet();
			rs.execute("select b.workcode,b.lastname from uf_IAMprivateKEY a left join hrmresource b on a.yh=b.id  where a.sy = '" + token + "'");
			if(rs.next()){
				result.put("status","0");
				result.put("msg","成功");
				result.put("employeeNum",rs.getString("workcode"));
				result.put("fullname",rs.getString("lastname"));
			}else{
				result.put("status","1");
				result.put("msg","未查询到该用户登录信息");
			}
		}

		return Response.ok();
	}
}
