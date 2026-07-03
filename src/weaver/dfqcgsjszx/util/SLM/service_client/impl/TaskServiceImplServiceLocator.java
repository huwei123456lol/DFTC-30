/**
 * TaskServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.SLM.service_client.impl;

import weaver.dfqcgsjszx.util.SLM.service_client.TaskService;
import weaver.general.BaseBean;

public class TaskServiceImplServiceLocator extends org.apache.axis.client.Service implements TaskServiceImplService {

    public TaskServiceImplServiceLocator() {
    }


    public TaskServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TaskServiceImplServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TaskServiceImplPort
    //private String TaskServiceImplPort_address = "http://10.4.10.204:8686/DFTC_PI_WS_Proxy/SLM_4_COS_WF_TASK";
    private String TaskServiceImplPort_address =new BaseBean().getPropValue("integration_address_config","slm_ws_url");

    public String getTaskServiceImplPortAddress() {
        return TaskServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String TaskServiceImplPortWSDDServiceName = "TaskServiceImplPort";

    public String getTaskServiceImplPortWSDDServiceName() {
        return TaskServiceImplPortWSDDServiceName;
    }

    public void setTaskServiceImplPortWSDDServiceName(String name) {
        TaskServiceImplPortWSDDServiceName = name;
    }

    public TaskService getTaskServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TaskServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTaskServiceImplPort(endpoint);
    }

    public TaskService getTaskServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TaskServiceImplServiceSoapBindingStub _stub = new TaskServiceImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTaskServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTaskServiceImplPortEndpointAddress(String address) {
        TaskServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (TaskService.class.isAssignableFrom(serviceEndpointInterface)) {
                TaskServiceImplServiceSoapBindingStub _stub = new TaskServiceImplServiceSoapBindingStub(new java.net.URL(TaskServiceImplPort_address), this);
                _stub.setPortName(getTaskServiceImplPortWSDDServiceName());
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
        if ("TaskServiceImplPort".equals(inputPortName)) {
            return getTaskServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.server.webservice.isoftstone.com/", "TaskServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.server.webservice.isoftstone.com/", "TaskServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("TaskServiceImplPort".equals(portName)) {
            setTaskServiceImplPortEndpointAddress(address);
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
