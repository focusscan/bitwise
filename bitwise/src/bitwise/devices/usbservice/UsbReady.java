package bitwise.devices.usbservice;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;

public final class UsbReady<H extends BaseDriverHandle<?, ?>, D extends BaseDriver<UsbDevice, H>> {
	private final UsbDevice device;
	private final UsbDriverFactory<H, D> factory;
	
	protected UsbReady(UsbDevice in_device, UsbDriverFactory<H, D> in_factory) {
		device = in_device;
		factory = in_factory;
	}
	
	public UsbDevice getDevice() {
		return device;
	}
	
	public UsbDriverFactory<H, D> getFactory() {
		return factory;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s", device.getProduct(), factory.getDriverName());
	}
}
