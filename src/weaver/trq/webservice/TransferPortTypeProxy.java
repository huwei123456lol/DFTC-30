package weaver.trq.webservice;

import java.rmi.RemoteException;

public class TransferPortTypeProxy implements TransferPortType {
	private String _endpoint = null;
	private TransferPortType transferPortType = null;

	public TransferPortTypeProxy() {
		_initTransferPortTypeProxy();
	}

	public TransferPortTypeProxy(String endpoint) {
		_endpoint = endpoint;
		_initTransferPortTypeProxy();
	}

	private void _initTransferPortTypeProxy() {
		try {
			transferPortType = (new TransferLocator()).getTransferHttpSoap11Endpoint();
			if (transferPortType != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) transferPortType)._setProperty("javax.xml.rpc.service.endpoint.address",
							_endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) transferPortType)
							._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (transferPortType != null)
			((javax.xml.rpc.Stub) transferPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public TransferPortType getTransferPortType() {
		if (transferPortType == null)
			_initTransferPortTypeProxy();
		return transferPortType;
	}

	public String send(int type, String content) throws RemoteException {
		if (transferPortType == null)
			_initTransferPortTypeProxy();
		return transferPortType.send(type, content);
	}

}