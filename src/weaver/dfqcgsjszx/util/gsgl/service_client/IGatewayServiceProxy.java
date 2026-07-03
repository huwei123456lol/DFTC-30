package weaver.dfqcgsjszx.util.gsgl.service_client;

public class IGatewayServiceProxy implements IGatewayService {
  private String _endpoint = null;
  private IGatewayService iGatewayService = null;
  
  public IGatewayServiceProxy() {
    _initIGatewayServiceProxy();
  }
  
  public IGatewayServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initIGatewayServiceProxy();
  }
  
  private void _initIGatewayServiceProxy() {
    try {
      iGatewayService = (new IGatewayServiceServiceLocator()).getIGatewayServicePort();
      if (iGatewayService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iGatewayService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iGatewayService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iGatewayService != null)
      ((javax.xml.rpc.Stub)iGatewayService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IGatewayService getIGatewayService() {
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService;
  }
  
  public String queryPhaseListByPro(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryPhaseListByPro(arg0);
  }
  
  public String queryCalendar(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryCalendar(arg0);
  }
  
  public String queryRemindMessageByUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryRemindMessageByUser(arg0);
  }
  
  public String operProjectSelect(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.operProjectSelect(arg0);
  }
  
  public String queryProjectList(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryProjectList(arg0);
  }
  
  public String operTaskByGroup(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.operTaskByGroup(arg0);
  }
  
  public String saveDeclareHour(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.saveDeclareHour(arg0);
  }
  
  public String queryTaskListByGroup(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryTaskListByGroup(arg0);
  }
  
  public String queryProjectListByUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryProjectListByUser(arg0);
  }
  
  public String queryWorkTaskListByPro(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryWorkTaskListByPro(arg0);
  }
  
  public String queryTaskListByUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryTaskListByUser(arg0);
  }
  
  public String operTaskByUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.operTaskByUser(arg0);
  }
  
  public String queryGroupUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryGroupUser(arg0);
  }
  
  public String queryTaskReportByUser(String arg0) throws java.rmi.RemoteException{
    if (iGatewayService == null)
      _initIGatewayServiceProxy();
    return iGatewayService.queryTaskReportByUser(arg0);
  }
  
  
}