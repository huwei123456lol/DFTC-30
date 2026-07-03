package weaver.trq.webservice.rlghsjy;

public class ProjectFileSoapProxy implements ProjectFileSoap {
  private String _endpoint = null;
  private ProjectFileSoap projectFileSoap = null;
  
  public ProjectFileSoapProxy() {
    _initProjectFileSoapProxy();
  }
  
  public ProjectFileSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initProjectFileSoapProxy();
  }
  
  private void _initProjectFileSoapProxy() {
    try {
      projectFileSoap = (new ProjectFileLocator()).getProjectFileSoap();
      if (projectFileSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)projectFileSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)projectFileSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (projectFileSoap != null)
      ((javax.xml.rpc.Stub)projectFileSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ProjectFileSoap getProjectFileSoap() {
    if (projectFileSoap == null)
      _initProjectFileSoapProxy();
    return projectFileSoap;
  }
  
  public String getFilesByProjectCode(String projectCode) throws java.rmi.RemoteException{
    if (projectFileSoap == null)
      _initProjectFileSoapProxy();
    return projectFileSoap.getFilesByProjectCode(projectCode);
  }
  
  public String updateFileStatus(int id, int status) throws java.rmi.RemoteException{
    if (projectFileSoap == null)
      _initProjectFileSoapProxy();
    return projectFileSoap.updateFileStatus(id, status);
  }
  
  
}