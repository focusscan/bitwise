package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbDevice;
import bitwise.engine.eventbus.Event;


public final class UsbDetachedEvent extends Event {
	private final UsbDevice device;
	
	public UsbDetachedEvent(UsbDevice in_device) {
		super("USB Device Detached");
		device = in_device;
	}
	
	public UsbDevice getDevice() {
		return device;
	}

	@Override
	public String getDescription() {
		return String.format("USB device (%s) detached.", device);
	}
}
