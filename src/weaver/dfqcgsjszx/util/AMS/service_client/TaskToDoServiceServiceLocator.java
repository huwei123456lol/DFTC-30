/**
 * TaskToDoServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.AMS.service_client;

import weaver.general.BaseBean;

import java.net.URL;

public class TaskToDoServiceServiceLocator extends org.apache.axis.client.Service implements TaskToDoServiceService {

    public TaskToDoServiceServiceLocator() {
    }


    public TaskToDoServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TaskToDoServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TaskToDoServicePort
//    private String TaskToDoServicePort_address = "http://10.4.10.204:8686/DFTC_PI_WS_Proxy/AMS_4_COS_WF_TASK";
    private String TaskToDoServicePort_address = new BaseBean().getPropValue("integration_address_config","ams_ws_url");


    public String getTaskToDoServicePortAddress() {
        return TaskToDoServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String TaskToDoServicePortWSDDServiceName = "TaskToDoServicePort";

    public String getTaskToDoServicePortWSDDServiceName() {
        return TaskToDoServicePortWSDDServiceName;
    }

    public void setTaskToDoServicePortWSDDServiceName(String name) {
        TaskToDoServicePortWSDDServiceName = name;
    }

    public ITaskToDoService getTaskToDoServicePort() throws javax.xml.rpc.ServiceException {
       URL endpoint;
        try {
            endpoint = new URL(TaskToDoServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTaskToDoServicePort(endpoint);
    }

    public ITaskToDoService getTaskToDoServicePort(URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TaskToDoServiceServiceSoapBindingStub _stub = new TaskToDoServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTaskToDoServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTaskToDoServicePortEndpointAddress(String address) {
        TaskToDoServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ITaskToDoService.class.isAssignableFrom(serviceEndpointInterface)) {
                TaskToDoServiceServiceSoapBindingStub _stub = new TaskToDoServiceServiceSoapBindingStub(new URL(TaskToDoServicePort_address), this);
                _stub.setPortName(getTaskToDoServicePortWSDDServiceName());
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
        if ("TaskToDoServicePort".equals(inputPortName)) {
            return getTaskToDoServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.isoftstone.com/", "TaskToDoServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.isoftstone.com/", "TaskToDoServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("TaskToDoServicePort".equals(portName)) {
            setTaskToDoServicePortEndpointAddress(address);
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
