/**
 * IGatewayService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.gsgl.service_client;

public interface IGatewayService extends java.rmi.Remote {
    public String queryPhaseListByPro(String arg0) throws java.rmi.RemoteException;
    public String queryCalendar(String arg0) throws java.rmi.RemoteException;
    public String queryRemindMessageByUser(String arg0) throws java.rmi.RemoteException;
    public String operProjectSelect(String arg0) throws java.rmi.RemoteException;
    public String queryProjectList(String arg0) throws java.rmi.RemoteException;
    public String operTaskByGroup(String arg0) throws java.rmi.RemoteException;
    public String saveDeclareHour(String arg0) throws java.rmi.RemoteException;
    public String queryTaskListByGroup(String arg0) throws java.rmi.RemoteException;
    public String queryProjectListByUser(String arg0) throws java.rmi.RemoteException;
    public String queryWorkTaskListByPro(String arg0) throws java.rmi.RemoteException;
    public String queryTaskListByUser(String arg0) throws java.rmi.RemoteException;
    public String operTaskByUser(String arg0) throws java.rmi.RemoteException;
    public String queryGroupUser(String arg0) throws java.rmi.RemoteException;
    public String queryTaskReportByUser(String arg0) throws java.rmi.RemoteException;
}
