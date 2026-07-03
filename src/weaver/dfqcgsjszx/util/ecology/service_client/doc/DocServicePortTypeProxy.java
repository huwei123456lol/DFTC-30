package weaver.dfqcgsjszx.util.ecology.service_client.doc;

public class DocServicePortTypeProxy implements DocServicePortType {
  private String _endpoint = null;
  private DocServicePortType docServicePortType = null;
  
  public DocServicePortTypeProxy() {
    _initDocServicePortTypeProxy();
  }
  
  public DocServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initDocServicePortTypeProxy();
  }
  
  private void _initDocServicePortTypeProxy() {
    try {
      docServicePortType = (new DocServiceLocator()).getDocServiceHttpPort();
      if (docServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)docServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)docServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (docServicePortType != null)
      ((javax.xml.rpc.Stub)docServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public DocServicePortType getDocServicePortType() {
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType;
  }
  
  public int deleteDoc(int in0, String in1) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.deleteDoc(in0, in1);
  }
  
  public int createDoc(DocInfo in0, String in1) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.createDoc(in0, in1);
  }
  
  public DocInfo[] getList(String in0, int in1, int in2) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.getList(in0, in1, in2);
  }
  
  public DocInfo getDoc(int in0, String in1) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.getDoc(in0, in1);
  }
  
  public int getDocCount(String in0) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.getDocCount(in0);
  }
  
  public String login(String in0, String in1, int in2, String in3) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.login(in0, in1, in2, in3);
  }
  
  public int updateDoc(DocInfo in0, String in1) throws java.rmi.RemoteException{
    if (docServicePortType == null)
      _initDocServicePortTypeProxy();
    return docServicePortType.updateDoc(in0, in1);
  }
  
  
}