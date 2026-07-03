/**
 * PatWaitReviewServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.IPMS.service_client;

import weaver.general.BaseBean;

public class PatWaitReviewServiceLocator extends org.apache.axis.client.Service implements PatWaitReviewService {

    public PatWaitReviewServiceLocator() {
    }


    public PatWaitReviewServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PatWaitReviewServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PatWaitReviewServiceSoap
    //private String PatWaitReviewServiceSoap_address = "http://10.4.10.204:8686/DFTC_PI_WS_Proxy/IPMS_4_COS_WF_TASK";
    private String PatWaitReviewServiceSoap_address = new BaseBean().getPropValue("integration_address_config","ipms_ws_url");

    public String getPatWaitReviewServiceSoapAddress() {
        return PatWaitReviewServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String PatWaitReviewServiceSoapWSDDServiceName = "PatWaitReviewServiceSoap";

    public String getPatWaitReviewServiceSoapWSDDServiceName() {
        return PatWaitReviewServiceSoapWSDDServiceName;
    }

    public void setPatWaitReviewServiceSoapWSDDServiceName(String name) {
        PatWaitReviewServiceSoapWSDDServiceName = name;
    }

    public PatWaitReviewServiceSoap getPatWaitReviewServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PatWaitReviewServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPatWaitReviewServiceSoap(endpoint);
    }

    public PatWaitReviewServiceSoap getPatWaitReviewServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PatWaitReviewServiceSoapStub _stub = new PatWaitReviewServiceSoapStub(portAddress, this);
            _stub.setPortName(getPatWaitReviewServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPatWaitReviewServiceSoapEndpointAddress(String address) {
        PatWaitReviewServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PatWaitReviewServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                PatWaitReviewServiceSoapStub _stub = new PatWaitReviewServiceSoapStub(new java.net.URL(PatWaitReviewServiceSoap_address), this);
                _stub.setPortName(getPatWaitReviewServiceSoapWSDDServiceName());
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
        if ("PatWaitReviewServiceSoap".equals(inputPortName)) {
            return getPatWaitReviewServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://daweisoft.org/", "PatWaitReviewService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://daweisoft.org/", "PatWaitReviewServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("PatWaitReviewServiceSoap".equals(portName)) {
            setPatWaitReviewServiceSoapEndpointAddress(address);
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
