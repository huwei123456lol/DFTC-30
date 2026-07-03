/**
 * PamProjectService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.PAM.project.service_client;

public interface PamProjectService extends java.rmi.Remote {
    public String getProject(String sysName) throws java.rmi.RemoteException;
    public String isSuccess(String sysName, String projCode) throws java.rmi.RemoteException;
}
