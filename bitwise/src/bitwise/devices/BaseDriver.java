package bitwise.devices;

import bitwise.engine.service.BaseService;

public abstract class BaseDriver<V extends Device, H extends BaseDriverHandle<?, ?>> extends BaseService<H> {
	private final BaseDriverCertificate cert = new BaseDriverCertificate();
	private final V device;
	
	protected BaseDriver(V in_device) {
		super();
		device = in_device;
	}
	
	protected V getDevice() {
		return device;
	}
	
	protected abstract boolean onStartDriver();
	
	protected final void deviceRequestsStopDriver(DeviceCertificate deviceCert) {
		if (null == deviceCert)
			throw new IllegalArgumentException("DeviceCertificate");
		this.stopServiceFromWithin();
	}

	@Override
	protected final boolean onStartService() {
		if (!device.setDriver(cert, this))
			return false;
		if (!onStartDriver()) {
			device.unsetDriver(cert);
			return false;
		}
		return true;
	}
	
	protected abstract void onStopDriver();

	@Override
	protected final void onStopService() {
		onStopDriver();
		device.unsetDriver(cert);
	}
}
