package weaver.dfqcgsjszx.util.PAM.project.service_client;

public class PamProjectServiceProxy implements PamProjectService {
  private String _endpoint = null;
  private PamProjectService pamProjectService = null;
  
  public PamProjectServiceProxy() {
    _initPamProjectServiceProxy();
  }
  
  public PamProjectServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPamProjectServiceProxy();
  }
  
  private void _initPamProjectServiceProxy() {
    try {
      pamProjectService = (new PamProjectServiceServiceLocator()).getPamProjectServicePort();
      if (pamProjectService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pamProjectService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pamProjectService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pamProjectService != null)
      ((javax.xml.rpc.Stub)pamProjectService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PamProjectService getPamProjectService() {
    if (pamProjectService == null)
      _initPamProjectServiceProxy();
    return pamProjectService;
  }
  
  public String getProject(String sysName) throws java.rmi.RemoteException{
    if (pamProjectService == null)
      _initPamProjectServiceProxy();
    return pamProjectService.getProject(sysName);
  }
  
  public String isSuccess(String sysName, String projCode) throws java.rmi.RemoteException{
    if (pamProjectService == null)
      _initPamProjectServiceProxy();
    return pamProjectService.isSuccess(sysName, projCode);
  }
  
  
}