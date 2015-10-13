package bitwise.devices;

import bitwise.engine.service.BaseService;

public abstract class BaseDriver<V extends Device, H extends BaseDriverHandle<?, ?>> extends BaseService<H> {
	private final V device;
	
	protected BaseDriver(V in_device) {
		super();
		device = in_device;
	}
	
	protected V getDevice() {
		return device;
	}
	
	protected abstract boolean onStartDriver();

	@Override
	protected final boolean onStartService() {
		if (!device.setDriver(this))
			return false;
		if (!onStartDriver()) {
			device.unsetDriver();
			return false;
		}
		return true;
	}
	
	protected abstract void onStopDriver();

	@Override
	protected final void onStopService() {
		onStopDriver();
		device.unsetDriver();
	}
}
