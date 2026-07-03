package weaver.dfqcgsjszx.util.AMS.service_client;

public class ITaskToDoServiceProxy implements ITaskToDoService {
  private String _endpoint = null;
  private ITaskToDoService iTaskToDoService = null;
  
  public ITaskToDoServiceProxy() {
    _initITaskToDoServiceProxy();
  }
  
  public ITaskToDoServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initITaskToDoServiceProxy();
  }
  
  private void _initITaskToDoServiceProxy() {
    try {
      iTaskToDoService = (new TaskToDoServiceServiceLocator()).getTaskToDoServicePort();
      if (iTaskToDoService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iTaskToDoService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iTaskToDoService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iTaskToDoService != null)
      ((javax.xml.rpc.Stub)iTaskToDoService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ITaskToDoService getITaskToDoService() {
    if (iTaskToDoService == null)
      _initITaskToDoServiceProxy();
    return iTaskToDoService;
  }
  
  public String taskToDo(String arg0) throws java.rmi.RemoteException{
    if (iTaskToDoService == null)
      _initITaskToDoServiceProxy();
    return iTaskToDoService.taskToDo(arg0);
  }
  
  
}