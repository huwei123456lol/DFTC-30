package weaver.trq.webservice.jhx;

public class TxServiceGatewayProxy implements TxServiceGateway_PortType {
  private String _endpoint = null;
  private TxServiceGateway_PortType txServiceGateway_PortType = null;
  
  public TxServiceGatewayProxy() {
    _initTxServiceGatewayProxy();
  }
  
  public TxServiceGatewayProxy(String endpoint) {
    _endpoint = endpoint;
    _initTxServiceGatewayProxy();
  }
  
  private void _initTxServiceGatewayProxy() {
    try {
      txServiceGateway_PortType = (new TxServiceGateway_ServiceLocator()).getTxServiceGatewayPort();
      if (txServiceGateway_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)txServiceGateway_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)txServiceGateway_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (txServiceGateway_PortType != null)
      ((javax.xml.rpc.Stub)txServiceGateway_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public TxServiceGateway_PortType getTxServiceGateway_PortType() {
    if (txServiceGateway_PortType == null)
      _initTxServiceGatewayProxy();
    return txServiceGateway_PortType;
  }
  
  public String send(String message) throws java.rmi.RemoteException{
    if (txServiceGateway_PortType == null)
      _initTxServiceGatewayProxy();
    return txServiceGateway_PortType.send(message);
  }
  
  
}