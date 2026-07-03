package com.engine.dfxm.service.syd;

import com.engine.core.impl.Service;
import com.engine.dfxm.cmd.dd.DDDoPoOrderCmd;
import com.engine.dfxm.cmd.dd.PoOrderFHCheckCmd;
import com.engine.dfxm.cmd.fhd.AcceptCheckCardCmd;
import com.engine.dfxm.cmd.fhd.CeptSrmDeliveryDetailCmd;
import com.engine.dfxm.cmd.fhd.ExportFHMXCmd;
import com.engine.dfxm.cmd.fhd.FHPoOrderDataCmd;
import com.engine.dfxm.cmd.fy.AcceptGiveUpCollection;
import com.engine.dfxm.cmd.jhfk.ChangeProcessState;
import com.engine.dfxm.cmd.shd.AcceptSupplierThirdPartyReceipt;
import com.engine.dfxm.cmd.syd.IndexOrderCheckReceiveCmd;
import com.engine.dfxm.cmd.syd.SYDDoIndexOrderCmd;
import com.engine.dfxm.cmd.thd.AcceptGoodsRuslut;
import weaver.hrm.User;

import java.util.Map;

/**
 *@ClassName SYDServiceImpl
 *@Description 请说明该类的作用
 *@Author 86157
 *@Date 2023-11-15 19:04
 *@Version 1.0
 **/
public class SYDServiceImpl extends Service implements SYDService{

    @Override
    public Map<String, Object> doIndexOrder(Map<String, Object> params, User user) {
        return commandExecutor.execute(new SYDDoIndexOrderCmd(params,user));
    }

    @Override
    public Map<String, Object> doPoOrder(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DDDoPoOrderCmd(params,user));
    }

    @Override
    public Map<String, Object> indexOrderCheckReceive(Map<String, Object> paramsMap, User user, String params) {
        return commandExecutor.execute(new IndexOrderCheckReceiveCmd(paramsMap,user,params));
    }

    @Override
    public Map<String, Object> ceptSrmDeliveryDetail(Map<String, Object> paramsMap, User user) {
        return commandExecutor.execute(new CeptSrmDeliveryDetailCmd(paramsMap,user));
    }

    @Override
    public Map<String, Object> acceptCheckCard(Map<String, Object> paramsMap, User user) {
        return commandExecutor.execute(new AcceptCheckCardCmd(paramsMap,user));
    }

    @Override
    public Map<String, Object> acceptSupplierThirdPartyReceipt(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new AcceptSupplierThirdPartyReceipt(request2Map,user));
    }

    @Override
    public Map<String, Object> getFHPoOrderCmd(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new FHPoOrderDataCmd(request2Map,user));
    }

    @Override
    public Map<String, Object> changeProcessState(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new ChangeProcessState(request2Map,user));
    }

    @Override
    public Map<String, Object> acceptGoodsRuslut(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new AcceptGoodsRuslut(request2Map,user));
    }

    @Override
    public Map<String, Object> acceptGiveUpCollection(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new AcceptGiveUpCollection(request2Map,user));
    }

    @Override
    public Map<String, Object> poOrderFHCheck(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new PoOrderFHCheckCmd(request2Map,user));
    }

    @Override
    public Map<String, Object> exportFHMX(Map<String, Object> request2Map, User user) {
        return commandExecutor.execute(new ExportFHMXCmd(request2Map,user));
    }


}
