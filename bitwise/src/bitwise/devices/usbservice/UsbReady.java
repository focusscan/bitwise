package bitwise.devices.usbservice;

import bitwise.devices.BaseDriverHandle;

public final class UsbReady<H extends BaseDriverHandle<?, ?>> {
	private final UsbDevice device;
	private final UsbDriverFactory<H> factory;
	
	protected UsbReady(UsbDevice in_device, UsbDriverFactory<H> in_factory) {
		device = in_device;
		factory = in_factory;
	}
	
	public UsbDevice getDevice() {
		return device;
	}
	
	public UsbDriverFactory<H> getFactory() {
		return factory;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s", device.getProduct(), factory.getDriverName());
	}
}
