/**
 * IAppGatewayServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.ycgl.service_client;

import weaver.general.BaseBean;

public class IAppGatewayServiceServiceLocator extends org.apache.axis.client.Service implements IAppGatewayServiceService {

    public IAppGatewayServiceServiceLocator() {
    }


    public IAppGatewayServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IAppGatewayServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IAppGatewayServicePort
//    private java.lang.String IAppGatewayServicePort_address = "http://10.4.12.54:8080/vlm/webservices/appGatewayService";
    private String IAppGatewayServicePort_address = new BaseBean().getPropValue("integration_address_config","old_vlm_ip");

    public String getIAppGatewayServicePortAddress() {
        return IAppGatewayServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String IAppGatewayServicePortWSDDServiceName = "IAppGatewayServicePort";

    public String getIAppGatewayServicePortWSDDServiceName() {
        return IAppGatewayServicePortWSDDServiceName;
    }

    public void setIAppGatewayServicePortWSDDServiceName(String name) {
        IAppGatewayServicePortWSDDServiceName = name;
    }

    public IAppGatewayService getIAppGatewayServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IAppGatewayServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIAppGatewayServicePort(endpoint);
    }

    public IAppGatewayService getIAppGatewayServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            IAppGatewayServiceServiceSoapBindingStub _stub = new IAppGatewayServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIAppGatewayServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIAppGatewayServicePortEndpointAddress(String address) {
        IAppGatewayServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IAppGatewayService.class.isAssignableFrom(serviceEndpointInterface)) {
                IAppGatewayServiceServiceSoapBindingStub _stub = new IAppGatewayServiceServiceSoapBindingStub(new java.net.URL(IAppGatewayServicePort_address), this);
                _stub.setPortName(getIAppGatewayServicePortWSDDServiceName());
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
        if ("IAppGatewayServicePort".equals(inputPortName)) {
            return getIAppGatewayServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.app.webservice.dawnpro.com/", "IAppGatewayServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.app.webservice.dawnpro.com/", "IAppGatewayServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("IAppGatewayServicePort".equals(portName)) {
            setIAppGatewayServicePortEndpointAddress(address);
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
