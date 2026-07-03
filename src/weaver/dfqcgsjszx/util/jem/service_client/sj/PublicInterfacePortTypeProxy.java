package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class PublicInterfacePortTypeProxy implements PublicInterfacePortType {
  private String _endpoint = null;
  private PublicInterfacePortType publicInterfacePortType = null;
  
  public PublicInterfacePortTypeProxy() {
    _initPublicInterfacePortTypeProxy();
  }
  
  public PublicInterfacePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initPublicInterfacePortTypeProxy();
  }
  
  private void _initPublicInterfacePortTypeProxy() {
    try {
      publicInterfacePortType = (new PublicInterfaceLocator()).getPublicInterfaceHttpPort();
      if (publicInterfacePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)publicInterfacePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)publicInterfacePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (publicInterfacePortType != null)
      ((javax.xml.rpc.Stub)publicInterfacePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PublicInterfacePortType getPublicInterfacePortType() {
    if (publicInterfacePortType == null)
      _initPublicInterfacePortTypeProxy();
    return publicInterfacePortType;
  }
  
  public SyncReceivePackage call(SendPackage in0) throws java.rmi.RemoteException{
    if (publicInterfacePortType == null)
      _initPublicInterfacePortTypeProxy();
    return publicInterfacePortType.call(in0);
  }
  
  public AsyncReceivePackage getAsyncResult(String in0) throws java.rmi.RemoteException{
    if (publicInterfacePortType == null)
      _initPublicInterfacePortTypeProxy();
    return publicInterfacePortType.getAsyncResult(in0);
  }
  
  
}