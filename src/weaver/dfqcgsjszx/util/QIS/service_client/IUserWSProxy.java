package weaver.dfqcgsjszx.util.QIS.service_client;

import weaver.dfqcgsjszx.util.QIS.service_client.impl.UserWSServiceLocator;

public class IUserWSProxy implements IUserWS {
  private String _endpoint = null;
  private IUserWS iUserWS = null;
  
  public IUserWSProxy() {
    _initIUserWSProxy();
  }
  
  public IUserWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initIUserWSProxy();
  }
  
  private void _initIUserWSProxy() {
    try {
      iUserWS = (new UserWSServiceLocator()).getUserWSPort();
      if (iUserWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iUserWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iUserWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iUserWS != null)
      ((javax.xml.rpc.Stub)iUserWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IUserWS getIUserWS() {
    if (iUserWS == null)
      _initIUserWSProxy();
    return iUserWS;
  }
  
  public String queryoanum(String arg0) throws java.rmi.RemoteException{
    if (iUserWS == null)
      _initIUserWSProxy();
    return iUserWS.queryoanum(arg0);
  }
  
  
}