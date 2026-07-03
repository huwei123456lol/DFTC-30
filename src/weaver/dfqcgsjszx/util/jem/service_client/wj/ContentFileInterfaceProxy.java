package weaver.dfqcgsjszx.util.jem.service_client.wj;

public class ContentFileInterfaceProxy implements ContentFileInterface {
  private String _endpoint = null;
  private ContentFileInterface contentFileInterface = null;
  
  public ContentFileInterfaceProxy() {
    _initContentFileInterfaceProxy();
  }
  
  public ContentFileInterfaceProxy(String endpoint) {
    _endpoint = endpoint;
    _initContentFileInterfaceProxy();
  }
  
  private void _initContentFileInterfaceProxy() {
    try {
      contentFileInterface = (new ContentFileInterfaceServiceLocator()).getContentFileInterfacePort();
      if (contentFileInterface != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)contentFileInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)contentFileInterface)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (contentFileInterface != null)
      ((javax.xml.rpc.Stub)contentFileInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ContentFileInterface getContentFileInterface() {
    if (contentFileInterface == null)
      _initContentFileInterfaceProxy();
    return contentFileInterface;
  }
  
  public BasicContent getFileContent(String arg0, String arg1, String arg2) throws java.rmi.RemoteException{
    if (contentFileInterface == null)
      _initContentFileInterfaceProxy();
    return contentFileInterface.getFileContent(arg0, arg1, arg2);
  }
  
  public String saveFile(String arg0, String arg1, String arg2, BasicContent arg3, Boolean arg4, String arg5, String arg6) throws java.rmi.RemoteException{
    if (contentFileInterface == null)
      _initContentFileInterfaceProxy();
    return contentFileInterface.saveFile(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
  }
  
  
}