/**
 * Transfer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.trq.webservice;

public interface Transfer extends javax.xml.rpc.Service {
    public String getTransferHttpSoap11EndpointAddress();

    public TransferPortType getTransferHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException;

    public TransferPortType getTransferHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
