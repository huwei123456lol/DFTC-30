/**
 * CommonServiceImplLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.tydlpt.service_client.impl;

import weaver.dfqcgsjszx.util.tydlpt.service_client.CommonService;
import weaver.general.BaseBean;

public class CommonServiceImplLocator extends org.apache.axis.client.Service implements CommonServiceImpl {

    public CommonServiceImplLocator() {
    }


    public CommonServiceImplLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CommonServiceImplLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CommonServiceImplPort
    //private String CommonServiceImplPort_address = "http://10.4.10.204:8180/PTService/ws/CommonService";
    private String CommonServiceImplPort_address =  new BaseBean().getPropValue("integration_address_config","tydlpt_ws_url");

    public String getCommonServiceImplPortAddress() {
        return CommonServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String CommonServiceImplPortWSDDServiceName = "CommonServiceImplPort";

    public String getCommonServiceImplPortWSDDServiceName() {
        return CommonServiceImplPortWSDDServiceName;
    }

    public void setCommonServiceImplPortWSDDServiceName(String name) {
        CommonServiceImplPortWSDDServiceName = name;
    }

    public CommonService getCommonServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CommonServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCommonServiceImplPort(endpoint);
    }

    public CommonService getCommonServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CommonServiceImplSoapBindingStub _stub = new CommonServiceImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getCommonServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCommonServiceImplPortEndpointAddress(String address) {
        CommonServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CommonService.class.isAssignableFrom(serviceEndpointInterface)) {
                CommonServiceImplSoapBindingStub _stub = new CommonServiceImplSoapBindingStub(new java.net.URL(CommonServiceImplPort_address), this);
                _stub.setPortName(getCommonServiceImplPortWSDDServiceName());
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
        if ("CommonServiceImplPort".equals(inputPortName)) {
            return getCommonServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.service.ws.model.common.dawnpro.com/", "CommonServiceImpl");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.service.ws.model.common.dawnpro.com/", "CommonServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("CommonServiceImplPort".equals(portName)) {
            setCommonServiceImplPortEndpointAddress(address);
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
