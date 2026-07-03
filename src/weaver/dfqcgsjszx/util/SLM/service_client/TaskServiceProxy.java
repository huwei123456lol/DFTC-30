package weaver.dfqcgsjszx.util.SLM.service_client;

import weaver.dfqcgsjszx.util.SLM.service_client.impl.TaskServiceImplServiceLocator;

public class TaskServiceProxy implements TaskService {
  private String _endpoint = null;
  private TaskService taskService = null;
  
  public TaskServiceProxy() {
    _initTaskServiceProxy();
  }
  
  public TaskServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initTaskServiceProxy();
  }
  
  private void _initTaskServiceProxy() {
    try {
      taskService = (new TaskServiceImplServiceLocator()).getTaskServiceImplPort();
      if (taskService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)taskService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)taskService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (taskService != null)
      ((javax.xml.rpc.Stub)taskService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public TaskService getTaskService() {
    if (taskService == null)
      _initTaskServiceProxy();
    return taskService;
  }
  
  public String taskToDo(String arg0) throws java.rmi.RemoteException{
    if (taskService == null)
      _initTaskServiceProxy();
    return taskService.taskToDo(arg0);
  }
  
  
}