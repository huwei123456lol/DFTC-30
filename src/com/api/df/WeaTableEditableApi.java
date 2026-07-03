package com.api.df;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author jzm
 * 集成登陆列表的展现和调整顺序
 */

@Path("/land")
public class WeaTableEditableApi {
    private BaseBean log = new BaseBean();

    @GET
    @Path("/getDatas")
    @Produces(MediaType.TEXT_PLAIN)
    //列表的展现
    public String getTableColumns(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        JSONArray datas = new JSONArray();
        try {
            RecordSet recordSet = new RecordSet();
            JSONObject jsonObject = new JSONObject();
            JSONObject object = null;

            User user = HrmUserVarify.getUser(request, response);
            String sql = "select cfg.id,cfg.xtid,sys.sys_xtmc,cfg.sx,sys.state from uf_unify_login_cfg cfg,uf_unify_login_sys sys where cfg.xtid=sys.id and cfg.ssyh='" + user.getUID() + "' order by cfg.sx";
            log.writeLog("sql=" + sql);
            recordSet.execute(sql);
            int order = 1;
            calData(datas, recordSet, order);
            jsonObject.put("datas", datas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas.toString();
    }

    @GET
    @Path("/updateOreder")
    @Produces(MediaType.TEXT_PLAIN)
    //调整列表顺序后更新数据库
    public String updateOreder(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException {
        log.writeLog("==========================updateOreder_Star==========================");
        JSONObject res = new JSONObject();
        try {
            RecordSet rs = new RecordSet();
            String datas = request.getParameter("datas");           //列表中的所有数据
            log.writeLog("datas=" + datas);
            JSONArray array = new JSONArray(datas);
            String sql = "";
            String id = "";

            for (int i = 0; i < array.length(); i++) {
                id = array.getJSONObject(i).getString("id");
                sql = "update uf_unify_login_cfg set sx = '" + (i + 1) + "' where id = '" + id + "'";
                rs.execute(sql);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.writeLog(getTrace(e));
            res.put("result", "-1");
            log.writeLog("res=" + res.toString());
            return res.toString();
        }
        res.put("result", "0");
        log.writeLog("res=" + res.toString());
        return res.toString();
    }

    @GET
    @Path("/initOrder")
    @Produces(MediaType.TEXT_PLAIN)
    //调整列表顺序后更新数据库
    public String initOrder(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        log.writeLog("==========================initOrder_Star==========================");
        JSONArray datas = new JSONArray();
        User user = HrmUserVarify.getUser(request, response);
        RecordSet rs = new RecordSet();
        JSONObject jsonObject = new JSONObject();

        String sql = "select cfg.id,cfg.xtid,sys.sys_xtmc,cfg.sx,sys.state from uf_unify_login_cfg cfg,uf_unify_login_sys sys where cfg.xtid=sys.id and cfg.ssyh='" + user.getUID() + "' order by sys.mrpx";
        log.writeLog("sql=" + sql);
        rs.execute(sql);
        int order = 1;
        JSONObject object = null;
        try {
            calData(datas, rs, order);
            jsonObject.put("datas", datas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas.toString();
    }

    private void calData(JSONArray datas, RecordSet rs, int order) throws JSONException {
        JSONObject object;
        while (rs.next()) {
            object = new JSONObject();
            object.put("id", rs.getString("id"));
            object.put("no", rs.getString("sx"));                                    //顺序
            object.put("order", order);                                                         //序号
            object.put("name", rs.getString("sys_xtmc"));                                 //系统名称
            try {
//                    new String("启用".getBytes("GBK"), "UTF-8")
                object.put("state", "0".equals(rs.getString("state")) ? new String("启用".getBytes("GBK"), "UTF-8") : new String("禁用".getBytes("GBK"), "UTF-8"));     //状态
            } catch (Exception e) {
                e.printStackTrace();
            }
            datas.put(object);
            order++;
        }
    }


    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
