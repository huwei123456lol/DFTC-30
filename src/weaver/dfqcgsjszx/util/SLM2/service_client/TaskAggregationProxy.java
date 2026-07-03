package weaver.dfqcgsjszx.util.SLM2.service_client;

public class TaskAggregationProxy implements TaskAggregation {
  private String _endpoint = null;
  private TaskAggregation taskAggregation = null;
  
  public TaskAggregationProxy() {
    _initTaskAggregationProxy();
  }
  
  public TaskAggregationProxy(String endpoint) {
    _endpoint = endpoint;
    _initTaskAggregationProxy();
  }
  
  private void _initTaskAggregationProxy() {
    try {
      taskAggregation = (new TaskAggregationServiceLocator()).getTaskAggregationPort();
      if (taskAggregation != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)taskAggregation)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)taskAggregation)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (taskAggregation != null)
      ((javax.xml.rpc.Stub)taskAggregation)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public TaskAggregation getTaskAggregation() {
    if (taskAggregation == null)
      _initTaskAggregationProxy();
    return taskAggregation;
  }
  
  public String getCount(String workcode) throws java.rmi.RemoteException{
    if (taskAggregation == null)
      _initTaskAggregationProxy();
    return taskAggregation.getCount(workcode);
  }
  
  
}