package bitwise.devices;

import bitwise.engine.Thing;

public abstract class Device extends Thing<DeviceID> {
	private BaseDriver<?, ?> driver = null;
	
	protected Device() {
		super(new DeviceID());
	}
	
	protected abstract void onSetDriver();
	
	protected synchronized boolean setDriver(BaseDriver<?, ?> in_driver) {
		if (null == driver) {
			driver = in_driver;
			onSetDriver();
			return true;
		}
		return false;
	}
	
	protected abstract void onUnsetDriver();
	
	protected synchronized void unsetDriver() {
		onUnsetDriver();
		driver = null;
	}
}
