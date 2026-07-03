/**
 * PamProjectServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.PAM.project.service_client;

import weaver.general.BaseBean;

public class PamProjectServiceServiceLocator extends org.apache.axis.client.Service implements PamProjectServiceService {

    public PamProjectServiceServiceLocator() {
    }


    public PamProjectServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PamProjectServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PamProjectServicePort
    //private String PamProjectServicePort_address = "http://10.4.11.118/super/services/PamProjectService";//正式接口地址
    private String PamProjectServicePort_address = new BaseBean().getPropValue("integration_address_config","pam_get_pro_ws_url");


    public String getPamProjectServicePortAddress() {
        return PamProjectServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String PamProjectServicePortWSDDServiceName = "PamProjectServicePort";

    public String getPamProjectServicePortWSDDServiceName() {
        return PamProjectServicePortWSDDServiceName;
    }

    public void setPamProjectServicePortWSDDServiceName(String name) {
        PamProjectServicePortWSDDServiceName = name;
    }

    public PamProjectService getPamProjectServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PamProjectServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPamProjectServicePort(endpoint);
    }

    public PamProjectService getPamProjectServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PamProjectServiceServiceSoapBindingStub _stub = new PamProjectServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getPamProjectServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPamProjectServicePortEndpointAddress(String address) {
        PamProjectServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PamProjectService.class.isAssignableFrom(serviceEndpointInterface)) {
                PamProjectServiceServiceSoapBindingStub _stub = new PamProjectServiceServiceSoapBindingStub(new java.net.URL(PamProjectServicePort_address), this);
                _stub.setPortName(getPamProjectServicePortWSDDServiceName());
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
        if ("PamProjectServicePort".equals(inputPortName)) {
            return getPamProjectServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.modules.pmo/", "PamProjectServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.modules.pmo/", "PamProjectServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("PamProjectServicePort".equals(portName)) {
            setPamProjectServicePortEndpointAddress(address);
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
