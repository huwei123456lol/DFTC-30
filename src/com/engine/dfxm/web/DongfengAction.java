package com.engine.dfxm.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.dfxm.service.syd.SYDService;
import com.engine.dfxm.service.syd.SYDServiceImpl;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class DongfengAction {
    private SYDService getService(User user) {
        return (SYDService) ServiceUtil.getService(SYDServiceImpl.class, user);
    }

    /**
     * 索引单确认取消接口
     * @author wangkun
     * @date 2024-10-16 15:12
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/doIndexOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String doIndexOrder(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.doIndexOrder(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 订单确认取消接口
     * @author wangkun
     * @date 2024-10-16 15:13
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/doPoOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String doPoOrder(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.doPoOrder(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 索引单发票校验接口-想从webserverice换成api的，目前没用到
     * @author wangkun
     * @date 2024-10-16 15:34
     * @param request
     * @param response
     * @param params
     * @return java.lang.String
     */
    @POST
    @Path("/indexOrderCheckReceive")
    @Produces(MediaType.APPLICATION_JSON)
    public String indexOrderCheckReceive(@Context HttpServletRequest request, @Context HttpServletResponse response,String params) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.indexOrderCheckReceive(ParamUtil.request2Map(request),user,params);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    
    /**
     * 发货单接口，推送发货单到360
     * @author wangkun
     * @date 2024-10-16 15:36
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/ceptSrmDeliveryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    public String ceptSrmDeliveryDetail(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.ceptSrmDeliveryDetail(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }
    
    
    /**
     * 检查卡数据推送接口，后面检查卡的内容没定好，先不要了，暂无调用
     * @author wangkun
     * @date 2024-10-16 15:47 
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/acceptCheckCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String acceptCheckCard(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.acceptCheckCard(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    
    /**
     * 第三方收货接口，推送第三方收货数据到360
     * @author wangkun
     * @date 2024-10-16 15:49 
     * @param request 
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/acceptSupplierThirdPartyReceipt")
    @Produces(MediaType.APPLICATION_JSON)
    public String acceptSupplierThirdPartyReceipt(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.acceptSupplierThirdPartyReceipt(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 获取订单可发货数据明细，用于发货单前端根据订单带出发货明细
     * @author wangkun
     * @date 2024-10-16 16:00 
     * @param request 
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/getFHPoOrderCmd")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFHPoOrderCmd(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.getFHPoOrderCmd(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 计划结果反馈推送360 ，暂未使用
     * @author wangkun
     * @date 2024-10-16 16:10
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/changeProcessState")
    @Produces(MediaType.APPLICATION_JSON)
    public String changeProcessState(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.changeProcessState(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 退货处置接口-暂未使用
     * @author wangkun
     * @date 2024-10-16 16:18
     * @param request 
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/acceptGoodsRuslut")
    @Produces(MediaType.APPLICATION_JSON)
    public String acceptGoodsRuslut(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.acceptGoodsRuslut(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 放弃收款接口-暂未使用
     * @author wangkun
     * @date 2024-10-16 16:25
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/acceptGiveUpCollection")
    @Produces(MediaType.APPLICATION_JSON)
    public String acceptGiveUpCollection(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.acceptGiveUpCollection(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 检查卡接口-暂停使用，检查卡推送换成附件上传，校验接口停止使用
     * @author wangkun
     * @date 2024-10-16 16:30
     * @param request
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/checkJCK")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkJCK(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            RecordSet rs=new RecordSet();
            Map<String,Object>  params=ParamUtil.request2Map(request);
            String id= Util.null2String(params.get("id"));
            rs.executeQuery("select * from uf_df_ddfh where id='"+id+"' and jcksftscg=0");
            if(rs.next()){
                apidatas.put("status", true);
                apidatas.put("msg","");
            }else{
                apidatas.put("status", false);
                apidatas.put("msg","检查卡未推送");
            }
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 订单发货校验
     * @author wangkun
     * @date 2024-10-16 16:39 
     * @param request 
     * @param response
     * @return java.lang.String
     */
    @POST
    @Path("/poOrderFHCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String poOrderFHCheck(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.poOrderFHCheck(ParamUtil.request2Map(request),user);
        } catch (Exception e) {
            apidatas.put("status", false);
            apidatas.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 需求未定，暂时不做了
     * @author wangkun
     * @date 2024-10-16 16:59
     * @param request
     * @param response
     * @return void
     */
    @POST
    @Path("/exportFHMX")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void exportFHMX(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            SYDService service=getService(user);
            apidatas=service.exportFHMX(ParamUtil.request2Map(request),user);
        } catch (Exception e) {

        }
    }

}
