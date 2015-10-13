package bitwise.devices;

import bitwise.engine.Thing;

public abstract class Device extends Thing<DeviceID> {
	private final DeviceCertificate cert = new DeviceCertificate();
	private BaseDriver<?, ?> driver = null;
	
	protected Device() {
		super(new DeviceID());
	}
	
	public final void deviceDetached() {
		if (null != driver) {
			driver.deviceRequestsStopDriver(cert);
		}
	}
	
	protected abstract void onSetDriver();
	
	protected synchronized boolean setDriver(BaseDriverCertificate driverCert, BaseDriver<?, ?> in_driver) {
		if (null == driverCert)
			throw new IllegalArgumentException("BaseDriverCertificate");
		if (null == driver) {
			driver = in_driver;
			onSetDriver();
			return true;
		}
		return false;
	}
	
	protected abstract void onUnsetDriver();
	
	protected synchronized void unsetDriver(BaseDriverCertificate driverCert) {
		if (null == driverCert)
			throw new IllegalArgumentException("BaseDriverCertificate");
		onUnsetDriver();
		driver = null;
	}
}
