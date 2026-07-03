/**
 * TaskToDoServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.AMS.service_client;

import java.net.URL;

public interface TaskToDoServiceService extends javax.xml.rpc.Service {
    public String getTaskToDoServicePortAddress();

    public ITaskToDoService getTaskToDoServicePort() throws javax.xml.rpc.ServiceException;

    public ITaskToDoService getTaskToDoServicePort(URL portAddress) throws javax.xml.rpc.ServiceException;
}
