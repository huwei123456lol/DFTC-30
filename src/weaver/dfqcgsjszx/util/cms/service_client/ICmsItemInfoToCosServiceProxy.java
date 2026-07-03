package weaver.dfqcgsjszx.util.cms.service_client;

public class ICmsItemInfoToCosServiceProxy implements ICmsItemInfoToCosService {
  private String _endpoint = null;
  private ICmsItemInfoToCosService iCmsItemInfoToCosService = null;
  
  public ICmsItemInfoToCosServiceProxy() {
    _initICmsItemInfoToCosServiceProxy();
  }
  
  public ICmsItemInfoToCosServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initICmsItemInfoToCosServiceProxy();
  }
  
  private void _initICmsItemInfoToCosServiceProxy() {
    try {
      iCmsItemInfoToCosService = (new ICmsItemInfoToCosServiceServiceLocator()).getICmsItemInfoToCosServicePort();
      if (iCmsItemInfoToCosService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iCmsItemInfoToCosService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iCmsItemInfoToCosService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iCmsItemInfoToCosService != null)
      ((javax.xml.rpc.Stub)iCmsItemInfoToCosService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ICmsItemInfoToCosService getICmsItemInfoToCosService() {
    if (iCmsItemInfoToCosService == null)
      _initICmsItemInfoToCosServiceProxy();
    return iCmsItemInfoToCosService;
  }
  
  public String getItemInfoByItemCode(String jsonStr) throws java.rmi.RemoteException{
    if (iCmsItemInfoToCosService == null)
      _initICmsItemInfoToCosServiceProxy();
    return iCmsItemInfoToCosService.getItemInfoByItemCode(jsonStr);
  }
  
  
}