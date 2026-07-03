/**
 * ISysNotifyTodoWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tfzj.notify.webservice;

public interface ISysNotifyTodoWebService extends java.rmi.Remote {
    public NotifyTodoAppResult updateTodo(NotifyTodoUpdateContext arg0) throws java.rmi.RemoteException, Exception;
    public NotifyTodoAppResult setTodoDone(NotifyTodoRemoveContext arg0) throws java.rmi.RemoteException, Exception;
    public NotifyTodoAppResult getTodo(NotifyTodoGetContext arg0) throws java.rmi.RemoteException, Exception;
    public NotifyTodoAppResult sendTodo(NotifyTodoSendContext arg0) throws java.rmi.RemoteException, Exception;
    public NotifyTodoAppResult deleteTodo(NotifyTodoRemoveContext arg0) throws java.rmi.RemoteException, Exception;
}
