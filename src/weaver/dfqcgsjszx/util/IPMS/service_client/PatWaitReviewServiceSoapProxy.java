package weaver.dfqcgsjszx.util.IPMS.service_client;

public class PatWaitReviewServiceSoapProxy implements PatWaitReviewServiceSoap {
  private String _endpoint = null;
  private PatWaitReviewServiceSoap patWaitReviewServiceSoap = null;
  
  public PatWaitReviewServiceSoapProxy() {
    _initPatWaitReviewServiceSoapProxy();
  }
  
  public PatWaitReviewServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initPatWaitReviewServiceSoapProxy();
  }
  
  private void _initPatWaitReviewServiceSoapProxy() {
    try {
      patWaitReviewServiceSoap = (new PatWaitReviewServiceLocator()).getPatWaitReviewServiceSoap();
      if (patWaitReviewServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)patWaitReviewServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)patWaitReviewServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (patWaitReviewServiceSoap != null)
      ((javax.xml.rpc.Stub)patWaitReviewServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PatWaitReviewServiceSoap getPatWaitReviewServiceSoap() {
    if (patWaitReviewServiceSoap == null)
      _initPatWaitReviewServiceSoapProxy();
    return patWaitReviewServiceSoap;
  }
  
  public String getJobCount(String id) throws java.rmi.RemoteException{
    if (patWaitReviewServiceSoap == null)
      _initPatWaitReviewServiceSoapProxy();
    return patWaitReviewServiceSoap.getJobCount(id);
  }
  
  public String getJobCountAndUrl(String workCode) throws java.rmi.RemoteException{
    if (patWaitReviewServiceSoap == null)
      _initPatWaitReviewServiceSoapProxy();
    return patWaitReviewServiceSoap.getJobCountAndUrl(workCode);
  }
  
  public String getJob(String id, int num) throws java.rmi.RemoteException{
    if (patWaitReviewServiceSoap == null)
      _initPatWaitReviewServiceSoapProxy();
    return patWaitReviewServiceSoap.getJob(id, num);
  }
  
  
}