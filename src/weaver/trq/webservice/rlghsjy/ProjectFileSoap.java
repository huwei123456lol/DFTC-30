/**
 * ProjectFileSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.trq.webservice.rlghsjy;

public interface ProjectFileSoap extends java.rmi.Remote {
    public String getFilesByProjectCode(String projectCode) throws java.rmi.RemoteException;
    public String updateFileStatus(int id, int status) throws java.rmi.RemoteException;
}
