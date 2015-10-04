package bitwise.devices.usb;

import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.drivers.UsbDriverFactory;

public class ReadyDevice<D extends UsbDriver> {
	private final UsbDevice device;
	private final UsbDriverFactory<D> driverFactory;
	
	public ReadyDevice(UsbDevice in_device, UsbDriverFactory<D> in_driverFactory) {
		device = in_device;
		driverFactory = in_driverFactory;
	}
	
	public UsbDevice getDevice() {
		return device;
	}
	
	public UsbDriverFactory<D> getDriverFactory() {
		return driverFactory;
	}
}
