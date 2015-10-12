package bitwise.devices.usbservice;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;

public final class UsbReady<R extends DriverRequest, H extends DriverHandle<R, ?>, D extends Driver<UsbDevice, R, H>> {
	private final UsbDevice device;
	private final UsbDriverFactory<R, H, D> factory;
	
	protected UsbReady(UsbDevice in_device, UsbDriverFactory<R, H, D> in_factory) {
		device = in_device;
		factory = in_factory;
	}
	
	public UsbDevice getDevice() {
		return device;
	}
	
	public UsbDriverFactory<R, H, D> getFactory() {
		return factory;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s", device.getProduct(), factory.getDriverName());
	}
}
