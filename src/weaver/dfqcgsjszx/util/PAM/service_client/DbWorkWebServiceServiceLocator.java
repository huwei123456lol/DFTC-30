/**
 * DbWorkWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.PAM.service_client;

import weaver.general.BaseBean;

public class DbWorkWebServiceServiceLocator extends org.apache.axis.client.Service implements DbWorkWebServiceService {

    public DbWorkWebServiceServiceLocator() {
    }


    public DbWorkWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DbWorkWebServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DbWorkWebServicePort
    //private String DbWorkWebServicePort_address = "http://10.4.10.204:8686/DFTC_PI_WS_Proxy/PAM_4_COS_WF_TASK";
    private String DbWorkWebServicePort_address =  new BaseBean().getPropValue("integration_address_config","pam_ws_url");

    public String getDbWorkWebServicePortAddress() {
        return DbWorkWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String DbWorkWebServicePortWSDDServiceName = "DbWorkWebServicePort";

    public String getDbWorkWebServicePortWSDDServiceName() {
        return DbWorkWebServicePortWSDDServiceName;
    }

    public void setDbWorkWebServicePortWSDDServiceName(String name) {
        DbWorkWebServicePortWSDDServiceName = name;
    }

    public DbWorkWebService getDbWorkWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DbWorkWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDbWorkWebServicePort(endpoint);
    }

    public DbWorkWebService getDbWorkWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DbWorkWebServiceServiceSoapBindingStub _stub = new DbWorkWebServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDbWorkWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDbWorkWebServicePortEndpointAddress(String address) {
        DbWorkWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (DbWorkWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                DbWorkWebServiceServiceSoapBindingStub _stub = new DbWorkWebServiceServiceSoapBindingStub(new java.net.URL(DbWorkWebServicePort_address), this);
                _stub.setPortName(getDbWorkWebServicePortWSDDServiceName());
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
        if ("DbWorkWebServicePort".equals(inputPortName)) {
            return getDbWorkWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.modules.pmo/", "DbWorkWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.modules.pmo/", "DbWorkWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("DbWorkWebServicePort".equals(portName)) {
            setDbWorkWebServicePortEndpointAddress(address);
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
