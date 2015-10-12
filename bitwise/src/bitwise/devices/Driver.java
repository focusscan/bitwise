package bitwise.devices;

import bitwise.engine.service.Service;

public abstract class Driver<D extends Device, R extends DriverRequest, H extends DriverHandle<R, ?>> extends Service<R, H> {
	private final D device;
	
	protected Driver(D in_device) {
		super();
		device = in_device;
	}
	
	protected D getDevice() {
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
