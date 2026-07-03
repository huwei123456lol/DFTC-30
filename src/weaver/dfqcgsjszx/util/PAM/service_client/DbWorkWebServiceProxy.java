package weaver.dfqcgsjszx.util.PAM.service_client;

public class DbWorkWebServiceProxy implements DbWorkWebService {
  private String _endpoint = null;
  private DbWorkWebService dbWorkWebService = null;
  
  public DbWorkWebServiceProxy() {
    _initDbWorkWebServiceProxy();
  }
  
  public DbWorkWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initDbWorkWebServiceProxy();
  }
  
  private void _initDbWorkWebServiceProxy() {
    try {
      dbWorkWebService = (new DbWorkWebServiceServiceLocator()).getDbWorkWebServicePort();
      if (dbWorkWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dbWorkWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dbWorkWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dbWorkWebService != null)
      ((javax.xml.rpc.Stub)dbWorkWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public DbWorkWebService getDbWorkWebService() {
    if (dbWorkWebService == null)
      _initDbWorkWebServiceProxy();
    return dbWorkWebService;
  }
  
  public String getPamDbWork(String workcode) throws java.rmi.RemoteException{
    if (dbWorkWebService == null)
      _initDbWorkWebServiceProxy();
    return dbWorkWebService.getPamDbWork(workcode);
  }
  
  
}