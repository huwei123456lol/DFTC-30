package weaver.dfqcgsjszx.service.impl;

import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.dfqcgsjszx.service.ContractInfoForEIPService;
import weaver.general.BaseBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同信息接口（供给EIP系统调用)
 *
 * @author Alex.Du
 */
public class ContractInfoForEIPServiceImpl implements ContractInfoForEIPService {
    /**
     * 查询合同信息
     *
     * @param modifyDate 更新日期
     * @return
     */
    public String searchContractInfo(String modifyDate){
        //查询采购合同基本信息表
        RecordSet rs =new RecordSet();
        RecordSet rs2 =new RecordSet();
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            //通过合同的更新日期，匹配查找合同信息
            rs.execute("select * from uf_cghtjbxx where gxrq='"+modifyDate+"'");
            List resultData = new ArrayList();
            while (rs.next()) {
                Map<String, Object> cghtMap = new HashMap<String, Object>();
                cghtMap.put("HTBH", rs.getString("htbh"));

                //查询当前合同中部门的编号、名称
                rs2.execute("select departmentcode,departmentname from hrmdepartment where id='" + rs.getString("sqbm") + "'");
                if(rs2.next()) {
                    cghtMap.put("QYBMBH", rs2.getString("departmentcode"));
                    cghtMap.put("QYBMZT", rs2.getString("departmentname"));
                }else{
                    cghtMap.put("QYBMBH", "");
                    cghtMap.put("QYBMZT", "");
                }

                cghtMap.put("HTZT", rs.getString("htzt"));
                cghtMap.put("HTZJE", rs.getFloat("htjehsy"));
                cghtMap.put("XMMC", "");
                cghtMap.put("XMBH", "");
                cghtMap.put("HTNR", rs.getString("htnrjfjnr"));

                //去供应商基本信息表，查询对应的供应商名称
                rs2.execute("select * uf_cggl_gysjbxxb where id='"+rs.getString("dfqydw")+"'");
                if(rs2.next()){
                    cghtMap.put("XDF", rs2.getString("gysmc"));
                }else{
                    cghtMap.put("XDF", "");
                }

                resultData.add(cghtMap);
                cghtMap = null;
            }

            result.put("STATUS",0);
            result.put("MSG","成功");
            result.put("DATA",resultData);
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("查询合同信息出现异常："+e.getMessage());
            result.put("STATUS",1);
            result.put("MSG","查询合同信息出现异常："+e.getMessage());
            result.put("DATA","");
        }

        rs = null;
        rs2 = null;

        JSONObject resultJson = new JSONObject(result);

        return resultJson.toString();
    }

    /**
     * 传递合同支付信息
     *
     * @param htbh 合同编号
     * @param fksqr 付款申请人
     * @param fkrq 付款日期
     * @param fkje 付款金额
     * @param fksy 付款事由
     * @param lcmc 流程名称
     * @param lcdh 流程单号
     * @return
     */
    public String toContractPayInfo(String htbh, String fksqr, String fkrq, float fkje, String fksy, String lcmc, String lcdh){
        //将获取到的合同支付信息，存入uf_cghtfkjl表
        RecordSet rs = new RecordSet();
        Map<String,Object> result = new HashMap<String,Object>();


        try {
            //通过合同编号，到合同基本信息表中查找对应的合同ID
            String htid = null;
            rs.execute("select id from uf_cghtjbxx where htbh='" + htbh + "'");
            if (rs.next()) {
                htid = rs.getString("id");
            }

            //通过付款申请人，到人力资源表中，查询人员ID
            String fksqrid = null;
            rs.execute("select id from hrmresource where workcode='" + fksqr + "'");
            if (rs.next()) {
                fksqrid = rs.getString("id");
            }

            rs.execute("insert into uf_cghtfkjl(htbh,htmc,fksqr,fkrq,fkje,fksy,lcmc,lcdh) values('" + htbh + "'," + htid + "," + fksqrid + ",'" + fkrq + "','" + fkje + "','" + fksy + "','" + lcmc + "','" + lcdh + "')");

            result.put("STATUS",0);
            result.put("MSG","成功");
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("传递合同支付信息出现异常："+e.getMessage());
            result.put("STATUS",0);
            result.put("MSG","传递合同支付信息出现异常："+e.getMessage());
        }

        JSONObject resultJson = new JSONObject(result);
        return resultJson.toString();
    }
}
