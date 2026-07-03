package com.engine.dfxm.util;

import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName DFXMModeUtil
 *@Description 请说明该类的作用
 *@Author 86157
 *@Date 2024-3-28 15:58
 *@Version 1.0
 **/
public class DFXMModeUtil {
    private static Logger logger= LoggerFactory.getLogger(DFXMModeUtil.class);


    /**
     * 批量计算订单待发货数量，发货占用数量
     * @author wangkun
     * @date 2024-3-28 18:04
     * @param ddhList 订单号集合
     * @return void
     */
    public static void calculateOrderNum(List<String> ddhList) {
        //采购订单接收：需要计算数量
        //    1.前面代码更新订单数量、冻结数量
        //    2.计算对应订单号的所有的已发起货数量和发货占用数量（创建发货单但是未发货），更新到订单明细
        try{
            for (int i = 0; i < ddhList.size(); i++) {
                String ddh=ddhList.get(i);
                calculateOrderNumByOrderCode(ddh);
            }
        }catch (Exception e){
            logger.error("更新订单数量失败:"+e);
        }
    }

    /**
     * 计算订单待发货数量，发货占用数量
     * @author wangkun
     * @date 2024-3-28 18:18
     * @param ddh 订单号
     * @return void
     */
    public static void calculateOrderNumByOrderCode(String ddh) {
        try{
            //String sql="select * from uf_cs_ddgl_dt1 where pkmain='"+sydList.get(i)+"'";
            //    2.1.根据订单号获取发货状态为已发货的明细数量，根据订单号和订单行号做分组汇总,根据订单号获取未发货的发货单明细数量（发货占用数量），
            //    根据订单号和订单行号做分组汇总
            //    2.2.根据订单号和订单行号更新订单里面的已发货数量和发货占用数量
            RecordSet rs=new RecordSet();
            String sql="";
            String upsql="";
            if("sqlserver".equals(rs.getDBType())){
                sql="UPDATE uf_cs_ddgl_dt1  " +
                        "SET yfhsl  =a.yfhsl,fhzysl=a.fhzysl " +
                        " from (select sum(t1.sfsl) as yfhsl,0 as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
                        "where t.ORDER_CODE='"+ddh+"' and fhzt=1 group by t.ORDER_CODE,LINE_NUMBER " +
                        "union all  " +
                        "select 0 as yfhsl,sum(t1.sfsl) as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
                        "where t.ORDER_CODE='"+ddh+"' and fhzt=0 group by t.ORDER_CODE,LINE_NUMBER " +
                        ") a  " +
                        "where uf_cs_ddgl_dt1.PKMain ='"+ddh+"' and  uf_cs_ddgl_dt1.PKMain  = a.ORDER_CODE and uf_cs_ddgl_dt1.LINE_NUMBER =a.LINE_NUMBER  " ;
                upsql="update uf_cs_ddgl_dt1 set dfhsl=(ORDER_QTY-ISNULL(LOCK_QTY,0)-ISNULL(yfhsl,0)-ISNULL(fhzysl,0))   where PKMain ='"+ddh+"'";
            }else if("mysql".equals(rs.getDBType())){
                sql="UPDATE uf_cs_ddgl_dt1  JOIN " +
                        "(select sum(t1.sfsl) as yfhsl,0 as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
                        "where t.ORDER_CODE='"+ddh+"' and fhzt=1 group by t.ORDER_CODE,LINE_NUMBER " +
                        "union all  " +
                        "select 0 as yfhsl,sum(t1.sfsl) as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
                        "where t.ORDER_CODE='"+ddh+"' and fhzt=0 group by t.ORDER_CODE,LINE_NUMBER " +
                        ") a  " +
                        "ON uf_cs_ddgl_dt1.PKMain  = a.ORDER_CODE and uf_cs_ddgl_dt1.LINE_NUMBER =a.LINE_NUMBER  " +
                        "SET uf_cs_ddgl_dt1.yfhsl  =a.yfhsl,uf_cs_ddgl_dt1.fhzysl=a.fhzysl " +
                        "WHERE uf_cs_ddgl_dt1.PKMain ='"+ddh+"' ";
                upsql="update uf_cs_ddgl_dt1 set dfhsl=(ORDER_QTY-IFNULL(LOCK_QTY,0)-IFNULL(yfhsl,0)-IFNULL(fhzysl,0))   where PKMain ='"+ddh+"'";
            }

            logger.info("更新订单数量sql1:"+sql);
            rs.executeUpdate(sql);
            //    3.计算订单号的待发货数量=订单数量-冻结数量-已发货数量-发货占用数量
            logger.info("更新订单数量sql2:"+upsql);
            rs.executeUpdate(upsql);
            //  4.更新订单里面的发货状态,查询明细里面的待发货数量、已发货数量、发货占用数量
            sql="select sum(dfhsl) dfhsl,sum(yfhsl) yfhsl,sum(fhzysl) as fhzysl from  uf_cs_ddgl_dt1  where PKMain='"+ddh+"'";
            rs.executeQuery(sql);
            if(rs.next()){
                //已发货或者发货占用大于0，代发货大于0，部分发货
                if((rs.getDouble("yfhsl")>0d||rs.getDouble("fhzysl")>0d)&&rs.getDouble("dfhsl")>0d){
                    //部分发货
                    //rs.executeUpdate("update uf_cs_ddgl set fhzt=1 where ORDER_CODE='"+ddh+"'");
                    //待发货数量小于等于0，全部发货
                }else if(rs.getDouble("dfhsl")<=0d){
                    //全部发货
                    //rs.executeUpdate("update uf_cs_ddgl set fhzt=2 where ORDER_CODE='"+ddh+"'");
                }else if(rs.getDouble("yfhsl")<=0d&&rs.getDouble("fhzysl")<=0d){
                    //rs.executeUpdate("update uf_cs_ddgl set fhzt=0 where ORDER_CODE='"+ddh+"'");
                }
            }
        }catch (Exception e){
            logger.error("更新订单数量失败:"+e);
        }
    }


    /**
     * 检验发货单是否可发货
     * @author wangkun
     * @date 2024-3-28 18:04
     * @param id 发货单单据id
     * @param ORDER_CODE 采购订单号
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String, Object> doCheckFHDSL(String id, String ORDER_CODE) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        //1.根据发货单id获取到订单
        //2.计算当前未发货的发货单的发货数量
        //3.计算订单的可发货数量=订单数量-冻结数量-已发货数量
        //4.如果发货数量<可发货数量 则允许发货,否则不允许发货
        calculateOrderNumByOrderCode(ORDER_CODE);
        String sql="";
        RecordSet rs=new RecordSet();
        if("mysql".equals(rs.getDBType())){
            sql="select  sfsl,ORDER_CODE,a.LINE_NUMBER,a1.MATTER_NAME ,a1.MATTER_CODE, " +
                    "(IFNULL(a1.ORDER_QTY,0)-IFNULL(a1.LOCK_QTY,0)-IFNULL(a1.yfhsl,0)) KFHSL from (select t.ORDER_CODE ,sum(t1.sfsl) sfsl ,t1.LINE_NUMBER " +
                    "from uf_df_ddfh t  " +
                    "left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid  " +
                    "where t.id=" +id+
                    " group by t.ORDER_CODE,t1.LINE_NUMBER) a  " +
                    " left join uf_cs_ddgl_dt1 a1 on a.ORDER_CODE =a1.PKMain and a.LINE_NUMBER =a1.LINE_NUMBER ";
        }else if("sqlserver".equals(rs.getDBType())){
            sql="select  sfsl,ORDER_CODE,a.LINE_NUMBER,a1.MATTER_NAME ,a1.MATTER_CODE, " +
                    "(ISNULL(a1.ORDER_QTY,0)-ISNULL(a1.LOCK_QTY,0)-ISNULL(a1.yfhsl,0)) KFHSL from (select t.ORDER_CODE ,sum(t1.sfsl) sfsl ,t1.LINE_NUMBER " +
                    "from uf_df_ddfh t  " +
                    "left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid  " +
                    "where t.id=" +id+
                    " group by t.ORDER_CODE,t1.LINE_NUMBER) a  " +
                    " left join uf_cs_ddgl_dt1 a1 on a.ORDER_CODE =a1.PKMain and a.LINE_NUMBER =a1.LINE_NUMBER ";
        }
        rs.executeQuery(sql);
        String msg="";
        reMap.put("status",true);
        while (rs.next()){
            double sfsl=rs.getDouble("sfsl");
            double KFHSL=rs.getDouble("KFHSL");
            if(sfsl>KFHSL){
                reMap.put("status",false);
                //   实发数量大于可发货数量，则说明数量超出，需要修改
                msg+="零件:"+rs.getString("MATTER_NAME")+"("+rs.getString("MATTER_CODE")+") 的可发货数量为"+KFHSL+",当前发货单发货数量为"+sfsl+"" +
                        "数量超出可发数量；";
            }
        }

        if(!"".equals(msg)){
            msg+="请修改发货单后再确认发货！";
        }
        reMap.put("msg",msg);
        return reMap;
    }

    /**
     * 根据发货单id更新订单数量信息
     * @author wangkun
     * @date 2024-3-28 18:24
     * @param id
     * @return void
     */
    public static void calculateOrderNumByFHOrderCode(String id) {
        String sql="select order_code from uf_df_ddfh where id="+id;
        RecordSet rs=new RecordSet();
        rs.executeQuery(sql);
        if(rs.next()){
            calculateOrderNumByOrderCode(rs.getString("order_code"));
        }
    }

    /**
     * 计算索引单不含税金额
     * @author wangkun
     * @date 2024-8-23 23:35 
     * @param keyList SAP订单号List
     * @return void
     */
    public static void calculateIndexOrder(List<String> keyList) {
        try {
            List<List>  upList=new ArrayList<>();
            for (int i=0;i<keyList.size();i++){
                List row=new ArrayList();
                row.add(keyList.get(i));
                upList.add(row);
            }
            RecordSet rs=new RecordSet();
            String sql="UPDATE uf_sydlb " +
                    "JOIN (select sum(SETTLEMENT_MONEY) SETTLEMENT_MONEY,mainid,SAP_ORDER_CODE from uf_sydlb_dt1 where SAP_ORDER_CODE=? group by SAP_ORDER_CODE,mainid) a " +
                    "ON uf_sydlb.id = a.mainid and uf_sydlb.SAP_ORDER_CODE=a.SAP_ORDER_CODE " +
                    "SET uf_sydlb.EXCLUDING_TAX_AMOUNT = a.SETTLEMENT_MONEY ";
            boolean isok=rs.executeBatchSql(sql,upList);
            if(!isok){
                logger.error("更新索引单不含税金额失败" + rs.getExceptionMsg());
            }
        } catch (Exception e) {
            logger.error("更新索引单不含税金额异常" + e);
        }
    }

    /**
     * 根据订单号更新索引单含税金额
     * @author wangkun
     * @date 2024-8-23 23:35 
     * @param keyList 订单号list
     * @return void
     */
    public static void calculateIndexOrderByOrderCode(List<String> keyList) {
        try {
            List<List>  upList=new ArrayList<>();
            for (int i=0;i<keyList.size();i++){
                List row=new ArrayList();
                row.add(keyList.get(i));
                upList.add(row);
            }
            RecordSet rs=new RecordSet();
            String sql="UPDATE uf_sydlb " +
                    "JOIN uf_cs_ddgl ON uf_sydlb.SAP_ORDER_CODE = uf_cs_ddgl.EBELN  and uf_cs_ddgl.ORDER_CODE=? " +
                    "SET uf_sydlb.INCLUDING_TAX_AMOUNT = uf_cs_ddgl.HSZJE  ";
            boolean isok=rs.executeBatchSql(sql,upList);
            if(!isok){
                logger.error("更新索引单含税金额失败" + rs.getExceptionMsg());
            }
        } catch (Exception e) {
            logger.error("更新索引单含税金额异常" + e);
        }

    }

    /**
     * 根据收货单号更新订单里面的收货数量等，目前不需要，不做开发
     * @author wangkun
     * @date 2024-10-16 14:43
     * @param ids
     * @return void
     */
    public static void calculateOrderNumBySHOrderCode(String ids) {
        /**
         * 1.更新发货单的实收数量
         **/
        calculateFHOrderNumBySH(ids);
         /**
         *
         * 2.更新订单的已收货数量，待收货数量
         **/
        calculateDDOrderNumBySH(ids);
    }

    /**
     * 更新发货单的实收数量
     * @param ids
     */
    public static void calculateFHOrderNumBySH(String ids) {
        //RecordSet rs=new RecordSet();
        //String sql="";
        //String upsql="";
        //if("sqlserver".equals(rs.getDBType())){
        //    sql="UPDATE uf_cs_ddgl_dt1  " +
        //            "SET yfhsl  =a.yfhsl,fhzysl=a.fhzysl " +
        //            " from (select sum(t1.sfsl) as yfhsl,0 as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
        //            "where t.ORDER_CODE='"+ddh+"' and fhzt=1 group by t.ORDER_CODE,LINE_NUMBER " +
        //            "union all  " +
        //            "select 0 as yfhsl,sum(t1.sfsl) as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
        //            "where t.ORDER_CODE='"+ddh+"' and fhzt=0 group by t.ORDER_CODE,LINE_NUMBER " +
        //            ") a  " +
        //            "where uf_cs_ddgl_dt1.PKMain ='"+ddh+"' and  uf_cs_ddgl_dt1.PKMain  = a.ORDER_CODE and uf_cs_ddgl_dt1.LINE_NUMBER =a.LINE_NUMBER  " ;
        //    upsql="update uf_cs_ddgl_dt1 set dfhsl=(ORDER_QTY-ISNULL(LOCK_QTY,0)-ISNULL(yfhsl,0)-ISNULL(fhzysl,0))   where PKMain ='"+ddh+"'";
        //}else if("mysql".equals(rs.getDBType())){
        //    sql="UPDATE uf_cs_ddgl_dt1  JOIN " +
        //            "(select sum(t1.sfsl) as yfhsl,0 as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
        //            "where t.ORDER_CODE='"+ddh+"' and fhzt=1 group by t.ORDER_CODE,LINE_NUMBER " +
        //            "union all  " +
        //            "select 0 as yfhsl,sum(t1.sfsl) as fhzysl,t.ORDER_CODE,t1.LINE_NUMBER  from uf_df_ddfh t left join uf_df_ddfh_dt1 t1 on t.id=t1.mainid   " +
        //            "where t.ORDER_CODE='"+ddh+"' and fhzt=0 group by t.ORDER_CODE,LINE_NUMBER " +
        //            ") a  " +
        //            "ON uf_cs_ddgl_dt1.PKMain  = a.ORDER_CODE and uf_cs_ddgl_dt1.LINE_NUMBER =a.LINE_NUMBER  " +
        //            "SET uf_cs_ddgl_dt1.yfhsl  =a.yfhsl,uf_cs_ddgl_dt1.fhzysl=a.fhzysl " +
        //            "WHERE uf_cs_ddgl_dt1.PKMain ='"+ddh+"' ";
        //    upsql="update uf_cs_ddgl_dt1 set dfhsl=(ORDER_QTY-IFNULL(LOCK_QTY,0)-IFNULL(yfhsl,0)-IFNULL(fhzysl,0))   where PKMain ='"+ddh+"'";
        //}
    }

    /**
     * 更新订单的已收货数量，待收货数量
     * @param ids
     */
    public static void calculateDDOrderNumBySH(String ids) {

    }
}
