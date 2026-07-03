package weaver.dfqcgsjszx.util.MAS.service_client;

public class OASoapProxy implements OASoap {
  private String _endpoint = null;
  private OASoap oASoap = null;
  
  public OASoapProxy() {
    _initOASoapProxy();
  }
  
  public OASoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initOASoapProxy();
  }
  
  private void _initOASoapProxy() {
    try {
      oASoap = (new OALocator()).getOASoap();
      if (oASoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oASoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oASoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oASoap != null)
      ((javax.xml.rpc.Stub)oASoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public OASoap getOASoap() {
    if (oASoap == null)
      _initOASoapProxy();
    return oASoap;
  }
  
  public String getTaskByEmpNo(String empNO) throws java.rmi.RemoteException{
    if (oASoap == null)
      _initOASoapProxy();
    return oASoap.getTaskByEmpNo(empNO);
  }
  
  
}