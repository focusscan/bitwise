package bitwise.devices.usb.drivers;

import java.util.ArrayList;
import java.util.Collection;

import bitwise.apps.App;
import bitwise.apps.Resource;
import bitwise.devices.kinds.DeviceKind;
import bitwise.devices.usb.ReadyDevice;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbRequest;

public class UsbGetDriverRequest<D extends UsbDriver, K extends DeviceKind> extends UsbRequest {
	private final App app;
	private final ReadyDevice<D> ready;
	private final Class<K> asKind;
	private D driver = null;
	private K driverAsKind = null;
	
	public UsbGetDriverRequest(App in_app, ReadyDevice<D> in_ready, Class<K> in_asKind) {
		super("UsbGetDriverRequest");
		app = in_app;
		ready = in_ready;
		asKind = in_asKind;
		assert(null != app);
		assert(null != ready);
		assert(null != asKind);
	}
	
	public synchronized D getDriver() {
		return driver;
	}
	
	public synchronized K getDriverAsKind() {
		return driverAsKind;
	}
	
	@Override
	public synchronized Collection<Resource> getNewResources() {
		if (null == driver)
			return new ArrayList<>(0);
		else {
			ArrayList<Resource> ret = new ArrayList<>(1);
			ret.add(0, driver);
			return ret;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void serveRequest(UsbContext ctx) throws InterruptedException {
		if (getRequestCanceled().get())
			return;
		UsbDevice device = ready.getDevice();
		UsbDriverFactory<D> factory = ready.getDriverFactory();
		synchronized(device) {
			if (device.getDeviceInUse().get())
				return;
			driver = factory.makeDriver(app, device);
			assert(null != driver);
			if (asKind.isAssignableFrom(driver.getClass())) {
				driverAsKind = (K)driver;
				device.setDriver(driver);
				boolean initResult = driver.initialize(ctx, device);
				if (!initResult) {
					driver.resourceClose();
					device.unsetDriver();
					driver = null;
					driverAsKind = null;
				}
			}
		}
	}
}
