/**
 * ContentFileInterfaceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.wj;

public class ContentFileInterfaceServiceLocator extends org.apache.axis.client.Service implements ContentFileInterfaceService {

    public ContentFileInterfaceServiceLocator() {
    }


    public ContentFileInterfaceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ContentFileInterfaceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ContentFileInterfacePort
    //private String ContentFileInterfacePort_address = "http://10.4.12.206:8180/PublicInterface/ws/ContentFileInterface";//测试接口地址
    private String ContentFileInterfacePort_address = "http://10.4.11.77/PublicInterface/ws/ContentFileInterface";//生成接口地址

    public String getContentFileInterfacePortAddress() {
        return ContentFileInterfacePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String ContentFileInterfacePortWSDDServiceName = "ContentFileInterfacePort";

    public String getContentFileInterfacePortWSDDServiceName() {
        return ContentFileInterfacePortWSDDServiceName;
    }

    public void setContentFileInterfacePortWSDDServiceName(String name) {
        ContentFileInterfacePortWSDDServiceName = name;
    }

    public ContentFileInterface getContentFileInterfacePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ContentFileInterfacePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getContentFileInterfacePort(endpoint);
    }

    public ContentFileInterface getContentFileInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ContentFileInterfaceServiceSoapBindingStub _stub = new ContentFileInterfaceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getContentFileInterfacePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setContentFileInterfacePortEndpointAddress(String address) {
        ContentFileInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ContentFileInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                ContentFileInterfaceServiceSoapBindingStub _stub = new ContentFileInterfaceServiceSoapBindingStub(new java.net.URL(ContentFileInterfacePort_address), this);
                _stub.setPortName(getContentFileInterfacePortWSDDServiceName());
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
        if ("ContentFileInterfacePort".equals(inputPortName)) {
            return getContentFileInterfacePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.connector.rd.dawnpro.com/", "ContentFileInterfaceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.connector.rd.dawnpro.com/", "ContentFileInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("ContentFileInterfacePort".equals(portName)) {
            setContentFileInterfacePortEndpointAddress(address);
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
