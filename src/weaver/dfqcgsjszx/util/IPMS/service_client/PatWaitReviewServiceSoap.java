/**
 * PatWaitReviewServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.IPMS.service_client;

public interface PatWaitReviewServiceSoap extends java.rmi.Remote {
    public String getJobCount(String id) throws java.rmi.RemoteException;
    public String getJobCountAndUrl(String workCode) throws java.rmi.RemoteException;
    public String getJob(String id, int num) throws java.rmi.RemoteException;
}
