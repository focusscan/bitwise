package bitwise.usbservice;

import bitwise.engine.service.Request;
import bitwise.engine.service.Service;
import bitwise.engine.supervisor.SupervisorCertificate;

public final class UsbService extends Service<UsbRequest, UsbServiceHandle> {
	private final UsbServiceHandle serviceHandle;
	private UsbManager manager = null;
	
	public UsbService(SupervisorCertificate supervisorCert) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		serviceHandle = new UsbServiceHandle(this);
	}
	
	@Override
	public UsbServiceHandle getServiceHandle() {
		return serviceHandle;
	}

	@Override
	protected boolean onStartService() {
		try {
			manager = new UsbManager(this);
			addServiceTask(manager);
			return true;
		} catch (LibUsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onStopService() {
		// Nothing to do here
	}

	@Override
	protected void onRequestComplete(Request in) {
	}
}
