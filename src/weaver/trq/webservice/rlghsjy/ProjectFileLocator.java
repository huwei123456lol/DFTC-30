/**
 * ProjectFileLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.trq.webservice.rlghsjy;

public class ProjectFileLocator extends org.apache.axis.client.Service implements ProjectFile {

    public ProjectFileLocator() {
    }


    public ProjectFileLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ProjectFileLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ProjectFileSoap
    private String ProjectFileSoap_address = "http://172.16.2.19/print/webservice/ProjectFile.asmx";

    public String getProjectFileSoapAddress() {
        return ProjectFileSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String ProjectFileSoapWSDDServiceName = "ProjectFileSoap";

    public String getProjectFileSoapWSDDServiceName() {
        return ProjectFileSoapWSDDServiceName;
    }

    public void setProjectFileSoapWSDDServiceName(String name) {
        ProjectFileSoapWSDDServiceName = name;
    }

    public ProjectFileSoap getProjectFileSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ProjectFileSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getProjectFileSoap(endpoint);
    }

    public ProjectFileSoap getProjectFileSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ProjectFileSoapStub _stub = new ProjectFileSoapStub(portAddress, this);
            _stub.setPortName(getProjectFileSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setProjectFileSoapEndpointAddress(String address) {
        ProjectFileSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ProjectFileSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                ProjectFileSoapStub _stub = new ProjectFileSoapStub(new java.net.URL(ProjectFileSoap_address), this);
                _stub.setPortName(getProjectFileSoapWSDDServiceName());
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
        if ("ProjectFileSoap".equals(inputPortName)) {
            return getProjectFileSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "ProjectFile");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "ProjectFileSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("ProjectFileSoap".equals(portName)) {
            setProjectFileSoapEndpointAddress(address);
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
