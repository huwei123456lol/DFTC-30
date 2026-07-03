/**
 * IGatewayServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.gsgl.service_client;

import weaver.general.BaseBean;

public class IGatewayServiceServiceLocator extends org.apache.axis.client.Service implements IGatewayServiceService {

    public IGatewayServiceServiceLocator() {
    }


    public IGatewayServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IGatewayServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IGatewayServicePort
    private String IGatewayServicePort_address = new BaseBean().getPropValue("integration_address_config","old_whs_ip");
//    private java.lang.String IGatewayServicePort_address = "http://10.4.10.113:8090/dfmpm/webservices/gatewayService";

    @Override
    public String getIGatewayServicePortAddress() {
        return IGatewayServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String IGatewayServicePortWSDDServiceName = "IGatewayServicePort";

    public String getIGatewayServicePortWSDDServiceName() {
        return IGatewayServicePortWSDDServiceName;
    }

    public void setIGatewayServicePortWSDDServiceName(String name) {
        IGatewayServicePortWSDDServiceName = name;
    }

    @Override
    public IGatewayService getIGatewayServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IGatewayServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIGatewayServicePort(endpoint);
    }

    @Override
    public IGatewayService getIGatewayServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            IGatewayServiceServiceSoapBindingStub _stub = new IGatewayServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIGatewayServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIGatewayServicePortEndpointAddress(String address) {
        IGatewayServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IGatewayService.class.isAssignableFrom(serviceEndpointInterface)) {
                IGatewayServiceServiceSoapBindingStub _stub = new IGatewayServiceServiceSoapBindingStub(new java.net.URL(IGatewayServicePort_address), this);
                _stub.setPortName(getIGatewayServicePortWSDDServiceName());
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
        if ("IGatewayServicePort".equals(inputPortName)) {
            return getIGatewayServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.gateway.webservice.dawnpro.com/", "IGatewayServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.gateway.webservice.dawnpro.com/", "IGatewayServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("IGatewayServicePort".equals(portName)) {
            setIGatewayServicePortEndpointAddress(address);
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
