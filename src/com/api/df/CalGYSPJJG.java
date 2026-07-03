package com.api.df;

import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/GYSPJJG")
public class CalGYSPJJG {
    @GET
    @Path("/calcAction")
    @Produces(MediaType.TEXT_PLAIN)
    public String calcAction(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException {
        new BaseBean().writeLog("[CalGYSPJJG m calcAction] ============ Start ==========");
        RecordSet rs = new RecordSet();
        JSONObject joResult = new JSONObject();
        try {
            new BaseBean().writeLog(rs.execute("UPDATE uf_cggl_gyspjjl SET ZZDF=nvl(ZYDF,0)*0.7 +  nvl(ZHDF,0)*0.3 + nvl(JKX,0) WHERE nvl(jsbs,0)<>1"));
            new BaseBean().writeLog(rs.execute("UPDATE uf_cggl_gyspjjl SET PJJGJB = (CASE WHEN ZZDF>=90 THEN 0 WHEN ZZDF>=80 AND ZZDF<90 THEN 1 WHEN ZZDF>=70 AND ZZDF<80 THEN 2 ELSE 3 END) WHERE nvl(jsbs,0)<>1"));
            new BaseBean().writeLog(rs.execute("UPDATE UF_CGGL_GYSJBXXB A SET A.QUN = (SELECT PJJGJB FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+1)) WHERE EXISTS (SELECT 1 FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+1))"));
            new BaseBean().writeLog(rs.execute("UPDATE UF_CGGL_GYSJBXXB A SET A.QN = (SELECT PJJGJB FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+2)) WHERE EXISTS (SELECT 1 FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+2))"));
            new BaseBean().writeLog(rs.execute("UPDATE UF_CGGL_GYSJBXXB A SET A.SQN = (SELECT PJJGJB FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+3)) WHERE EXISTS (SELECT 1 FROM uf_cggl_gyspjjl B WHERE A.ID=B.GYSMC AND  to_char(SYSDATE,'YYYY') = to_char(B.PJND+3))"));
            new BaseBean().writeLog(rs.execute("UPDATE UF_CGGL_GYSJBXXB A SET A.lxsnpjyzgys = (CASE WHEN a.QUN = 0 AND a.QN=0 AND sqn=0 THEN 0 ELSE 1 END)"));
            new BaseBean().writeLog(rs.execute("UPDATE UF_CGGL_GYSJBXXB A SET A.gyszt = 9 WHERE id IN (SELECT gysmc FROM uf_cggl_gyspjjl WHERE pjjgjb = 3)"));
            joResult.put("status",true);
        } catch (Exception e) {
            e.printStackTrace();
            joResult.put("status",false);
            joResult.put("msg",e.getMessage());
        }
        new BaseBean().writeLog("[CalGYSPJJG m calcAction] ============ End ==========");


        return joResult.toString();
    }
}
