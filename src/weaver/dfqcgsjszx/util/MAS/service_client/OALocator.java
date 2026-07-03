/**
 * OALocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.MAS.service_client;

import weaver.general.BaseBean;

public class OALocator extends org.apache.axis.client.Service implements OA {

    public OALocator() {
    }


    public OALocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OALocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OASoap
    //private String OASoap_address = "http://10.4.10.204:8686/DFTC_PI_WS_Proxy/MAS_4_COS_WF_TASK";
    private String OASoap_address =  new BaseBean().getPropValue("integration_address_config","mas_ws_url");


    public String getOASoapAddress() {
        return OASoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String OASoapWSDDServiceName = "OASoap";

    public String getOASoapWSDDServiceName() {
        return OASoapWSDDServiceName;
    }

    public void setOASoapWSDDServiceName(String name) {
        OASoapWSDDServiceName = name;
    }

    public OASoap getOASoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OASoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOASoap(endpoint);
    }

    public OASoap getOASoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            OASoapStub _stub = new OASoapStub(portAddress, this);
            _stub.setPortName(getOASoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOASoapEndpointAddress(String address) {
        OASoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (OASoap.class.isAssignableFrom(serviceEndpointInterface)) {
                OASoapStub _stub = new OASoapStub(new java.net.URL(OASoap_address), this);
                _stub.setPortName(getOASoapWSDDServiceName());
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
        if ("OASoap".equals(inputPortName)) {
            return getOASoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "OA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "OASoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("OASoap".equals(portName)) {
            setOASoapEndpointAddress(address);
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
