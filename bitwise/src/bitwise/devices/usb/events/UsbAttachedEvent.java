package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbDevice;
import bitwise.engine.eventbus.Event;


public final class UsbAttachedEvent extends Event {
	private final UsbDevice device;
	
	public UsbAttachedEvent(UsbDevice in_device) {
		super("USB Device Attached");
		device = in_device;
	}
	
	public UsbDevice getDevice() {
		return device;
	}

	@Override
	public String getDescription() {
		return String.format("USB device (%s) attached.", device);
	}
}
