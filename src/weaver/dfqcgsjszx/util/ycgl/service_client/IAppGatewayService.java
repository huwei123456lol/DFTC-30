/**
 * IAppGatewayService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.ycgl.service_client;

public interface IAppGatewayService extends java.rmi.Remote {
    public String searchRecord(String jsonStr) throws java.rmi.RemoteException;
    public String waitDetails(String jsonStr) throws java.rmi.RemoteException;
    public String getDetailByID(String jsonStr) throws java.rmi.RemoteException;
    public String doStorage(String jsonStr, DoStorageSignaturePicsEntry[] signaturePics, DoStorageCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException;
    public String chkAccess(String jsonStr) throws java.rmi.RemoteException;
    public String waitListAndCount(String jsonStr) throws java.rmi.RemoteException;
    public String checkOperInfo(String jsonStr) throws java.rmi.RemoteException;
    public String doWait(String jsonStr) throws java.rmi.RemoteException;
    public String doReturnAndTransfer(String jsonStr, DoReturnAndTransferSignaturePicsEntry[] signaturePics, DoReturnAndTransferCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException;
    public String recordSubmit(String jsonStr, RecordSubmitCarPhotosEntry[] carPhotos) throws java.rmi.RemoteException;
    public String doInventory(String jsonStr) throws java.rmi.RemoteException;
}
