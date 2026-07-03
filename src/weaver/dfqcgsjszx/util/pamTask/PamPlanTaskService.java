/**
 * PamPlanTaskService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.pamTask;

public interface PamPlanTaskService extends java.rmi.Remote {
    public String isSuccess(String sysName, String taskCode) throws java.rmi.RemoteException;
    public String getPlanTask(String sysName) throws java.rmi.RemoteException;
}
