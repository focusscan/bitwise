package bitwise.devices.usb;

import bitwise.apps.App;
import bitwise.devices.kinds.DeviceKind;

public class UsbGetDriverRequest<D extends UsbDriver, K extends DeviceKind> extends UsbRequest {
	private final App app;
	private final ReadyDevice<D> ready;
	private final Class<K> asKind;
	private D driver = null;
	private K driverAsKind = null;
	private boolean terminated = false;
	
	public UsbGetDriverRequest(App in_app, ReadyDevice<D> in_ready, Class<K> in_asKind) {
		super("UsbGetDriverRequest");
		app = in_app;
		ready = in_ready;
		asKind = in_asKind;
		assert(null != app);
		assert(null != ready);
		assert(null != asKind);
	}
	
	public synchronized void cancelOrTerminate() {
		if (!terminated) {
			if (null != driver)
				driver.disableDriver();
			terminated = true;
		}
	}
	
	public synchronized D getDriver() {
		return driver;
	}
	
	public synchronized K getDriverAsKind() {
		return driverAsKind;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void serveRequest() {
		if (terminated)
			return;
		UsbDevice device = ready.getDevice();
		UsbDriverFactory<D> factory = ready.getDriverFactory();
		synchronized(device) {
			if (device.inUse())
				return;
			driver = factory.makeDriver(app, device);
			assert(null != driver);
			device.setDriver(driver);
			if (asKind.isAssignableFrom(driver.getClass())) {
				driverAsKind = (K)driver;
			}
			else {
				driver.disableDriver();
				driver = null;
				return;
			}
		}
	}
}
