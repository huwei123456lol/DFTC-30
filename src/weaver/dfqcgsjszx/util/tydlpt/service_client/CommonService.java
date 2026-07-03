/**
 * CommonService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.tydlpt.service_client;

public interface CommonService extends java.rmi.Remote {
    public Result modifyPasswd(String operaterId, String newPassword) throws java.rmi.RemoteException;
    public Result saveOrganizationInfo(OrgVO[] orgList, String systemId) throws java.rmi.RemoteException;
    public Result modifyPassword(String operaterId, String oldPassword, String newPassword) throws java.rmi.RemoteException;
    public Result login(String operaterId, String password) throws java.rmi.RemoteException;
    public Result saveOperaterInfo(OperaterVO[] operList, String systemId) throws java.rmi.RemoteException;
    public Result getAllOrganizationInfo(String systemId) throws java.rmi.RemoteException;
    public Result getAllOperaterInfo(String systemId) throws java.rmi.RemoteException;
}
