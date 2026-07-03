package com.engine.dfxm.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface FormModeService {
    /**
     * 接收采购订单
     * @param xml
     * @return
     */
    @WebMethod(operationName = "poOrderReceive" ,action = "urn:com.engine.dfxm.webservice.FormModeService.poOrderReceive")
    String poOrderReceive(String xml);

    /**
     * 关闭订单
     * @param xml
     * @return
     */
    @WebMethod(operationName = "poOrderClose" ,action = "urn:com.engine.dfxm.webservice.FormModeService.poOrderClose")
    String poOrderClose(String xml);

    /**
     * 接收索引单
     * @param xml
     * @return
     */
    @WebMethod(operationName = "indexOrderReceive" ,action = "urn:com.engine.dfxm.webservice.FormModeService.indexOrderReceive")
    String indexOrderReceive(String xml);

    /**
     * 接收索引单发票检验
     * @param xml
     * @return
     */
    @WebMethod(operationName = "indexOrderCheckReceive" ,action = "urn:com.engine.dfxm.webservice.FormModeService.indexOrderCheckReceive")
    String indexOrderCheckReceive(String xml);

    /**
     * 计划跟踪待办
     * @param json
     * @return
     */
    @WebMethod(operationName = "planTodoReceive" ,action = "urn:com.engine.dfxm.webservice.FormModeService.planTodoReceive")
    String planTodoReceive(String json);

    /**
     * 收货确认
     * @param json
     * @return
     */
    @WebMethod(operationName = "receiveConfirm" ,action = "urn:com.engine.dfxm.webservice.FormModeService.receiveConfirm")
    String receiveConfirm(String json);

    /**
     * 退货申请
     * @param json
     * @return
     */
    @WebMethod(operationName = "cancelGoods" ,action = "urn:com.engine.dfxm.webservice.FormModeService.cancelGoods")
    String cancelGoods(String json);
}
