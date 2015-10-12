package bitwise.devices;

import bitwise.engine.Thing;

public abstract class Device extends Thing<DeviceID> {
	private Driver<?, ?, ?> driver = null;
	
	protected Device() {
		super(new DeviceID());
	}
	
	protected synchronized boolean setDriver(Driver<?, ?, ?> in_driver) {
		if (null == driver) {
			driver = in_driver;
			return true;
		}
		return false;
	}
	
	protected synchronized void unsetDriver() {
		driver = null;
	}
}
