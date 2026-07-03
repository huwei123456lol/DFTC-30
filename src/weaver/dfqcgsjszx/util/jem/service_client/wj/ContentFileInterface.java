/**
 * ContentFileInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.wj;

public interface ContentFileInterface extends java.rmi.Remote {
    public BasicContent getFileContent(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String saveFile(String arg0, String arg1, String arg2, BasicContent arg3, Boolean arg4, String arg5, String arg6) throws java.rmi.RemoteException;
}
