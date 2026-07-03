/**
 * TransferLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.trq.webservice;

public class TransferLocator extends org.apache.axis.client.Service implements Transfer {

    public TransferLocator() {
    }


    public TransferLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransferLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TransferHttpSoap11Endpoint
    private String TransferHttpSoap11Endpoint_address = "http://119.97.216.10:8090/services/Transfer.TransferHttpSoap11Endpoint/";

    public String getTransferHttpSoap11EndpointAddress() {
        return TransferHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private String TransferHttpSoap11EndpointWSDDServiceName = "TransferHttpSoap11Endpoint";

    public String getTransferHttpSoap11EndpointWSDDServiceName() {
        return TransferHttpSoap11EndpointWSDDServiceName;
    }

    public void setTransferHttpSoap11EndpointWSDDServiceName(String name) {
        TransferHttpSoap11EndpointWSDDServiceName = name;
    }

    public TransferPortType getTransferHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TransferHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransferHttpSoap11Endpoint(endpoint);
    }

    public TransferPortType getTransferHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TransferSoap11BindingStub _stub = new TransferSoap11BindingStub(portAddress, this);
            _stub.setPortName(getTransferHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransferHttpSoap11EndpointEndpointAddress(String address) {
        TransferHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (TransferPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                TransferSoap11BindingStub _stub = new TransferSoap11BindingStub(new java.net.URL(TransferHttpSoap11Endpoint_address), this);
                _stub.setPortName(getTransferHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("TransferHttpSoap11Endpoint".equals(inputPortName)) {
            return getTransferHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.trq.weaver", "Transfer");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.trq.weaver", "TransferHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("TransferHttpSoap11Endpoint".equals(portName)) {
            setTransferHttpSoap11EndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
