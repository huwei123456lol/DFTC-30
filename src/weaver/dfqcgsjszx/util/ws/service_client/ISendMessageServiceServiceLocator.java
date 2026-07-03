/**
 * ISendMessageServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.ws.service_client;

import weaver.general.BaseBean;

public class ISendMessageServiceServiceLocator extends org.apache.axis.client.Service implements ISendMessageServiceService {

    public ISendMessageServiceServiceLocator() {
    }


    public ISendMessageServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ISendMessageServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ISendMessageServicePort
    //private String ISendMessageServicePort_address = "http://10.4.9.35:8686/DFTC_PI_WS_Proxy/EXTERNAL_AFFAIRS_4_COS";
    private String ISendMessageServicePort_address =  new BaseBean().getPropValue("integration_address_config","ws_ws_url");

    public String getISendMessageServicePortAddress() {
        return ISendMessageServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String ISendMessageServicePortWSDDServiceName = "ISendMessageServicePort";

    public String getISendMessageServicePortWSDDServiceName() {
        return ISendMessageServicePortWSDDServiceName;
    }

    public void setISendMessageServicePortWSDDServiceName(String name) {
        ISendMessageServicePortWSDDServiceName = name;
    }

    public ISendMessageService getISendMessageServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ISendMessageServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getISendMessageServicePort(endpoint);
    }

    public ISendMessageService getISendMessageServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ISendMessageServiceServiceSoapBindingStub _stub = new ISendMessageServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getISendMessageServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setISendMessageServicePortEndpointAddress(String address) {
        ISendMessageServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ISendMessageService.class.isAssignableFrom(serviceEndpointInterface)) {
                ISendMessageServiceServiceSoapBindingStub _stub = new ISendMessageServiceServiceSoapBindingStub(new java.net.URL(ISendMessageServicePort_address), this);
                _stub.setPortName(getISendMessageServicePortWSDDServiceName());
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
        if ("ISendMessageServicePort".equals(inputPortName)) {
            return getISendMessageServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.esb.dawnpro.com/", "ISendMessageServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.esb.dawnpro.com/", "ISendMessageServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("ISendMessageServicePort".equals(portName)) {
            setISendMessageServicePortEndpointAddress(address);
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
