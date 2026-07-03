package weaver.dfqcgsjszx.util.EIP.service_client;

public class ISendMessageServiceProxy implements ISendMessageService {
  private String _endpoint = null;
  private ISendMessageService iSendMessageService = null;
  
  public ISendMessageServiceProxy() {
    _initISendMessageServiceProxy();
  }
  
  public ISendMessageServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initISendMessageServiceProxy();
  }
  
  private void _initISendMessageServiceProxy() {
    try {
      iSendMessageService = (new ISendMessageServiceServiceLocator()).getISendMessageServicePort();
      if (iSendMessageService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iSendMessageService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iSendMessageService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iSendMessageService != null)
      ((javax.xml.rpc.Stub)iSendMessageService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ISendMessageService getISendMessageService() {
    if (iSendMessageService == null)
      _initISendMessageServiceProxy();
    return iSendMessageService;
  }
  
  public String sendMessage(String baseParams, String bizParams) throws java.rmi.RemoteException{
    if (iSendMessageService == null)
      _initISendMessageServiceProxy();
    return iSendMessageService.sendMessage(baseParams, bizParams);
  }
  
  
}