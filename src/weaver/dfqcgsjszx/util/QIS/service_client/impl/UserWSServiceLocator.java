/**
 * UserWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.QIS.service_client.impl;

import weaver.dfqcgsjszx.util.QIS.service_client.IUserWS;
import weaver.general.BaseBean;

public class UserWSServiceLocator extends org.apache.axis.client.Service implements UserWSService {

    public UserWSServiceLocator() {
    }


    public UserWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserWSServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UserWSPort
    //private String UserWSPort_address = "http://10.5.70.88:8080/webservice/webService/userWS";
    private String UserWSPort_address =new BaseBean().getPropValue("integration_address_config","qis_ws_url");

    public String getUserWSPortAddress() {
        return UserWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String UserWSPortWSDDServiceName = "UserWSPort";

    public String getUserWSPortWSDDServiceName() {
        return UserWSPortWSDDServiceName;
    }

    public void setUserWSPortWSDDServiceName(String name) {
        UserWSPortWSDDServiceName = name;
    }

    public IUserWS getUserWSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserWSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserWSPort(endpoint);
    }

    public IUserWS getUserWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            UserWSServiceSoapBindingStub _stub = new UserWSServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getUserWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserWSPortEndpointAddress(String address) {
        UserWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IUserWS.class.isAssignableFrom(serviceEndpointInterface)) {
                UserWSServiceSoapBindingStub _stub = new UserWSServiceSoapBindingStub(new java.net.URL(UserWSPort_address), this);
                _stub.setPortName(getUserWSPortWSDDServiceName());
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
        if ("UserWSPort".equals(inputPortName)) {
            return getUserWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.ws.com/", "UserWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.ws.com/", "UserWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("UserWSPort".equals(portName)) {
            setUserWSPortEndpointAddress(address);
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
