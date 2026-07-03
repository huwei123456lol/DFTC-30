package weaver.trq.webservice;

public interface TransferPortType  extends java.rmi.Remote {
	public String send(int type,String content) throws java.rmi.RemoteException;
}
