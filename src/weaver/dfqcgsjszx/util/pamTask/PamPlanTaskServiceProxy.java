package weaver.dfqcgsjszx.util.pamTask;

public class PamPlanTaskServiceProxy implements PamPlanTaskService {
  private String _endpoint = null;
  private PamPlanTaskService pamPlanTaskService = null;
  
  public PamPlanTaskServiceProxy() {
    _initPamPlanTaskServiceProxy();
  }
  
  public PamPlanTaskServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPamPlanTaskServiceProxy();
  }
  
  private void _initPamPlanTaskServiceProxy() {
    try {
      pamPlanTaskService = (new PamPlanTaskServiceServiceLocator()).getPamPlanTaskServicePort();
      if (pamPlanTaskService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pamPlanTaskService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pamPlanTaskService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pamPlanTaskService != null)
      ((javax.xml.rpc.Stub)pamPlanTaskService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PamPlanTaskService getPamPlanTaskService() {
    if (pamPlanTaskService == null)
      _initPamPlanTaskServiceProxy();
    return pamPlanTaskService;
  }
  
  public String isSuccess(String sysName, String taskCode) throws java.rmi.RemoteException{
    if (pamPlanTaskService == null)
      _initPamPlanTaskServiceProxy();
    return pamPlanTaskService.isSuccess(sysName, taskCode);
  }
  
  public String getPlanTask(String sysName) throws java.rmi.RemoteException{
    if (pamPlanTaskService == null)
      _initPamPlanTaskServiceProxy();
    return pamPlanTaskService.getPlanTask(sysName);
  }
  
  
}