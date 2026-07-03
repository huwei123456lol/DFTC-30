package weaver.dfqcgsjszx.util.tydlpt.service_client;

import weaver.dfqcgsjszx.util.tydlpt.service_client.impl.CommonServiceImplLocator;

public class CommonServiceProxy implements CommonService {
  private String _endpoint = null;
  private CommonService commonService = null;
  
  public CommonServiceProxy() {
    _initCommonServiceProxy();
  }
  
  public CommonServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonServiceProxy();
  }
  
  private void _initCommonServiceProxy() {
    try {
      commonService = (new CommonServiceImplLocator()).getCommonServiceImplPort();
      if (commonService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonService != null)
      ((javax.xml.rpc.Stub)commonService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public CommonService getCommonService() {
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService;
  }
  
  public Result modifyPasswd(String operaterId, String newPassword) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.modifyPasswd(operaterId, newPassword);
  }
  
  public Result saveOrganizationInfo(OrgVO[] orgList, String systemId) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.saveOrganizationInfo(orgList, systemId);
  }
  
  public Result modifyPassword(String operaterId, String oldPassword, String newPassword) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.modifyPassword(operaterId, oldPassword, newPassword);
  }
  
  public Result login(String operaterId, String password) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.login(operaterId, password);
  }
  
  public Result saveOperaterInfo(OperaterVO[] operList, String systemId) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.saveOperaterInfo(operList, systemId);
  }
  
  public Result getAllOrganizationInfo(String systemId) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.getAllOrganizationInfo(systemId);
  }
  
  public Result getAllOperaterInfo(String systemId) throws java.rmi.RemoteException{
    if (commonService == null)
      _initCommonServiceProxy();
    return commonService.getAllOperaterInfo(systemId);
  }
  
  
}