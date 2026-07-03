package com.weaver.rlww.workflow.webservices;

public class WorkflowServicePortTypeProxy implements WorkflowServicePortType {
  private String _endpoint = null;
  private WorkflowServicePortType workflowServicePortType = null;
  
  public WorkflowServicePortTypeProxy() {
    _initWorkflowServicePortTypeProxy();
  }
  
  public WorkflowServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initWorkflowServicePortTypeProxy();
  }
  
  private void _initWorkflowServicePortTypeProxy() {
    try {
      workflowServicePortType = (new WorkflowServiceLocator()).getWorkflowServiceHttpPort();
      if (workflowServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)workflowServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)workflowServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (workflowServicePortType != null)
      ((javax.xml.rpc.Stub)workflowServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public WorkflowServicePortType getWorkflowServicePortType() {
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType;
  }
  
  public WorkflowRequestInfo getWorkflowRequest(int in0, int in1, int in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequest(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getHendledWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo getWorkflowRequest4Split(int in0, int in1, int in2, int in3) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequest4Split(in0, in1, in2, in3);
  }
  
  public String submitWorkflowRequest(WorkflowRequestInfo in0, int in1, int in2, String in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.submitWorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public int getMyWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestCount4OS(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getCCWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestList4OS(in0, in1, in2, in3, in4, in5);
  }
  
  public String getLeaveDays(String in0, String in1, String in2, String in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getLeaveDays(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getHendledWorkflowRequestList4Ofs(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestList4Ofs(in0, in1, in2, in3, in4, in5);
  }
  
  public WorkflowBaseInfo[] getCreateWorkflowList(int in0, int in1, int in2, int in3, int in4, String[] in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowList(in0, in1, in2, in3, in4, in5);
  }
  
  public int getProcessedWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestCount(in0, in1);
  }
  
  public String doCreateRequest(WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.doCreateRequest(in0, in1);
  }
  
  public String doCreateWorkflowRequest(WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.doCreateWorkflowRequest(in0, in1);
  }
  
  public int getToDoWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestCount4OS(in0, in1, in2);
  }
  
  public String doForceOver(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.doForceOver(in0, in1);
  }
  
  public WorkflowRequestInfo[] getProcessedWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo getCreateWorkflowRequestInfo(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowRequestInfo(in0, in1);
  }
  
  public WorkflowBaseInfo[] getCreateWorkflowTypeList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowTypeList(in0, in1, in2, in3, in4);
  }
  
  public int getHendledWorkflowRequestCount4Ofs(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestCount4Ofs(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getMyWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestList4OS(in0, in1, in2, in3, in4, in5);
  }
  
  public WorkflowRequestInfo[] getProcessedWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestList4OS(in0, in1, in2, in3, in4, in5);
  }
  
  public int getForwardWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getForwardWorkflowRequestCount(in0, in1);
  }
  
  public int getToDoWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestCount(in0, in1);
  }
  
  public String givingOpinions(int in0, int in1, String in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.givingOpinions(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getCCWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public int getProcessedWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getProcessedWorkflowRequestCount4OS(in0, in1, in2);
  }
  
  public int getBeRejectWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getBeRejectWorkflowRequestCount(in0, in1);
  }
  
  public String forward2WorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.forward2WorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getAllWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getAllWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getToDoWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public int getCCWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestCount4OS(in0, in1, in2);
  }
  
  public int getHendledWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getHendledWorkflowRequestCount(in0, in1);
  }
  
  public WorkflowRequestInfo[] getToDoWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToDoWorkflowRequestList4OS(in0, in1, in2, in3, in4, in5);
  }
  
  public int getToBeReadWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToBeReadWorkflowRequestCount(in0, in1, in2);
  }
  
  public int getCreateWorkflowCount(int in0, int in1, String[] in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowCount(in0, in1, in2);
  }
  
  public String forwardWorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.forwardWorkflowRequest(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getToBeReadWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getToBeReadWorkflowRequestList(in0, in1, in2, in3, in4, in5);
  }
  
  public WorkflowRequestInfo[] getBeRejectWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getBeRejectWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public int getCCWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCCWorkflowRequestCount(in0, in1);
  }
  
  public int getAllWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getAllWorkflowRequestCount(in0, in1);
  }
  
  public int getDoingWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getDoingWorkflowRequestCount(in0, in1, in2);
  }
  
  public WorkflowRequestInfo[] getForwardWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getForwardWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public WorkflowRequestInfo[] getMyWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestList(in0, in1, in2, in3, in4);
  }
  
  public int getMyWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getMyWorkflowRequestCount(in0, in1);
  }
  
  public String[] getWorkflowNewFlag(String[] in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowNewFlag(in0, in1);
  }
  
  public void writeWorkflowReadFlag(String in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    workflowServicePortType.writeWorkflowReadFlag(in0, in1);
  }
  
  public int getCreateWorkflowTypeCount(int in0, String[] in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getCreateWorkflowTypeCount(in0, in1);
  }
  
  public WorkflowRequestLog[] getWorkflowRequestLogs(String in0, String in1, int in2, int in3, int in4) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getWorkflowRequestLogs(in0, in1, in2, in3, in4);
  }
  
  public boolean deleteRequest(int in0, int in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.deleteRequest(in0, in1);
  }
  
  public String getUserId(String in0, String in1) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getUserId(in0, in1);
  }
  
  public WorkflowRequestInfo[] getDoingWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException{
    if (workflowServicePortType == null)
      _initWorkflowServicePortTypeProxy();
    return workflowServicePortType.getDoingWorkflowRequestList(in0, in1, in2, in3, in4, in5);
  }
  
  
}