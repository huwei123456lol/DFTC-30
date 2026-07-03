/**
 * UserWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.QIS.service_client.impl;

import weaver.dfqcgsjszx.util.QIS.service_client.IUserWS;

public interface UserWSService extends javax.xml.rpc.Service {
    public String getUserWSPortAddress();

    public IUserWS getUserWSPort() throws javax.xml.rpc.ServiceException;

    public IUserWS getUserWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
