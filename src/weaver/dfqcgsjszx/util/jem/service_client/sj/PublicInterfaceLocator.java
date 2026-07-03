/**
 * PublicInterfaceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class PublicInterfaceLocator extends org.apache.axis.client.Service implements PublicInterface {

    public PublicInterfaceLocator() {
    }


    public PublicInterfaceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PublicInterfaceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PublicInterfaceHttpPort
    //private String PublicInterfaceHttpPort_address = "http://10.4.12.206:8180/PublicInterfaceV1/ws/PublicInterface";//测试接口地址
    private String PublicInterfaceHttpPort_address = "http://10.4.11.77/PublicInterfaceV1/ws/PublicInterface";//生产接口地址

    public String getPublicInterfaceHttpPortAddress() {
        return PublicInterfaceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String PublicInterfaceHttpPortWSDDServiceName = "PublicInterfaceHttpPort";

    public String getPublicInterfaceHttpPortWSDDServiceName() {
        return PublicInterfaceHttpPortWSDDServiceName;
    }

    public void setPublicInterfaceHttpPortWSDDServiceName(String name) {
        PublicInterfaceHttpPortWSDDServiceName = name;
    }

    public PublicInterfacePortType getPublicInterfaceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PublicInterfaceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPublicInterfaceHttpPort(endpoint);
    }

    public PublicInterfacePortType getPublicInterfaceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PublicInterfaceHttpBindingStub _stub = new PublicInterfaceHttpBindingStub(portAddress, this);
            _stub.setPortName(getPublicInterfaceHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPublicInterfaceHttpPortEndpointAddress(String address) {
        PublicInterfaceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PublicInterfacePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                PublicInterfaceHttpBindingStub _stub = new PublicInterfaceHttpBindingStub(new java.net.URL(PublicInterfaceHttpPort_address), this);
                _stub.setPortName(getPublicInterfaceHttpPortWSDDServiceName());
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
        if ("PublicInterfaceHttpPort".equals(inputPortName)) {
            return getPublicInterfaceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.connector.rd.dawnpro.com", "PublicInterface");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.connector.rd.dawnpro.com", "PublicInterfaceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("PublicInterfaceHttpPort".equals(portName)) {
            setPublicInterfaceHttpPortEndpointAddress(address);
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
