package weaver.dfqcgsjszx.util.ycgl.service_client;

public class IAppGatewayServiceProxy implements IAppGatewayService {
  private String _endpoint = null;
  private IAppGatewayService iAppGatewayService = null;
  
  public IAppGatewayServiceProxy() {
    _initIAppGatewayServiceProxy();
  }
  
  public IAppGatewayServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initIAppGatewayServiceProxy();
  }
  
  private void _initIAppGatewayServiceProxy() {
    try {
      iAppGatewayService = (new IAppGatewayServiceServiceLocator()).getIAppGatewayServicePort();
      if (iAppGatewayService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iAppGatewayService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iAppGatewayService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iAppGatewayService != null)
      ((javax.xml.rpc.Stub)iAppGatewayService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IAppGatewayService getIAppGatewayService() {
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService;
  }
  
  public String searchRecord(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.searchRecord(jsonStr);
  }
  
  public String waitDetails(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.waitDetails(jsonStr);
  }
  
  public String getDetailByID(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.getDetailByID(jsonStr);
  }
  
  public String doStorage(String jsonStr, DoStorageSignaturePicsEntry[] signaturePics, DoStorageCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.doStorage(jsonStr, signaturePics, carPhotos);
  }
  
  public String chkAccess(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.chkAccess(jsonStr);
  }
  
  public String waitListAndCount(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.waitListAndCount(jsonStr);
  }
  
  public String checkOperInfo(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.checkOperInfo(jsonStr);
  }
  
  public String doWait(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.doWait(jsonStr);
  }
  
  public String doReturnAndTransfer(String jsonStr, DoReturnAndTransferSignaturePicsEntry[] signaturePics, DoReturnAndTransferCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.doReturnAndTransfer(jsonStr, signaturePics, carPhotos);
  }
  
  public String recordSubmit(String jsonStr, RecordSubmitCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.recordSubmit(jsonStr, carPhotos);
  }
  
  public String doInventory(String jsonStr) throws java.rmi.RemoteException{
    if (iAppGatewayService == null)
      _initIAppGatewayServiceProxy();
    return iAppGatewayService.doInventory(jsonStr);
  }
  
  
}