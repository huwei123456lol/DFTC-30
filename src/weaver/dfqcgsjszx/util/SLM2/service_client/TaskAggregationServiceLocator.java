/**
 * TaskAggregationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.SLM2.service_client;

public class TaskAggregationServiceLocator extends org.apache.axis.client.Service implements TaskAggregationService {

    public TaskAggregationServiceLocator() {
    }


    public TaskAggregationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TaskAggregationServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TaskAggregationPort
    private String TaskAggregationPort_address = "http://10.4.11.154:9906/WebService/TaskAggregation";

    public String getTaskAggregationPortAddress() {
        return TaskAggregationPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String TaskAggregationPortWSDDServiceName = "TaskAggregationPort";

    public String getTaskAggregationPortWSDDServiceName() {
        return TaskAggregationPortWSDDServiceName;
    }

    public void setTaskAggregationPortWSDDServiceName(String name) {
        TaskAggregationPortWSDDServiceName = name;
    }

    public TaskAggregation getTaskAggregationPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TaskAggregationPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTaskAggregationPort(endpoint);
    }

    public TaskAggregation getTaskAggregationPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TaskAggregationServiceSoapBindingStub _stub = new TaskAggregationServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTaskAggregationPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTaskAggregationPortEndpointAddress(String address) {
        TaskAggregationPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (TaskAggregation.class.isAssignableFrom(serviceEndpointInterface)) {
                TaskAggregationServiceSoapBindingStub _stub = new TaskAggregationServiceSoapBindingStub(new java.net.URL(TaskAggregationPort_address), this);
                _stub.setPortName(getTaskAggregationPortWSDDServiceName());
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
        if ("TaskAggregationPort".equals(inputPortName)) {
            return getTaskAggregationPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.econage.com/", "TaskAggregationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.econage.com/", "TaskAggregationPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("TaskAggregationPort".equals(portName)) {
            setTaskAggregationPortEndpointAddress(address);
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
