package com.engine.dfxm.service.syd;


import weaver.hrm.User;

import java.util.Map;

public interface SYDService {
    Map<String, Object> doIndexOrder(Map<String, Object> params, User user);
    Map<String, Object> doPoOrder(Map<String, Object> params, User user);
    Map<String, Object> indexOrderCheckReceive(Map<String, Object> paramsMap, User user,String params);
    Map<String, Object> ceptSrmDeliveryDetail(Map<String, Object> request2Map, User user);
    Map<String, Object> acceptCheckCard(Map<String, Object> request2Map, User user);
    Map<String, Object> acceptSupplierThirdPartyReceipt(Map<String, Object> request2Map, User user);
    Map<String, Object> getFHPoOrderCmd(Map<String, Object> request2Map, User user);
    Map<String, Object> changeProcessState(Map<String, Object> request2Map, User user);
    Map<String, Object> acceptGoodsRuslut(Map<String, Object> request2Map, User user);
    Map<String, Object> acceptGiveUpCollection(Map<String, Object> request2Map, User user);
    Map<String, Object> poOrderFHCheck(Map<String, Object> request2Map, User user);
    Map<String, Object> exportFHMX(Map<String, Object> request2Map, User user);
}
