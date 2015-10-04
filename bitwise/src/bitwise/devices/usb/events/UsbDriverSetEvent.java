package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.engine.eventbus.Event;


public final class UsbDriverSetEvent extends Event {
	private final UsbDevice device;
	private final UsbDriver driver;
	
	public UsbDriverSetEvent(UsbDevice in_device, UsbDriver in_driver) {
		super("USB Driver Set");
		device = in_device;
		driver = in_driver;
	}
	
	public UsbDevice getDevice() {
		return device;
	}
	
	public UsbDriver getDriver() {
		return driver;
	}

	@Override
	public String getDescription() {
		return String.format("USB device (%s) has driver (%s) set.", device, driver);
	}
}
