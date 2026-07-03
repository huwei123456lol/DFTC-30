/**
 * PublicInterfacePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public interface PublicInterfacePortType extends java.rmi.Remote {
    public SyncReceivePackage call(SendPackage in0) throws java.rmi.RemoteException;
    public AsyncReceivePackage getAsyncResult(String in0) throws java.rmi.RemoteException;
}
