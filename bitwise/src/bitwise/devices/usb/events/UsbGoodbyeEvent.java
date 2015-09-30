package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbService;
import bitwise.engine.eventbus.Event;


public final class UsbGoodbyeEvent extends Event {
	private final UsbService usb;
	
	public UsbGoodbyeEvent(UsbService in_usb) {
		super("USB Goodbye");
		usb = in_usb;
		assert(null != usb);
	}
	
	@Override
	public String getDescription() {
		return String.format("USB [%s] shutting down. No new requests will be served; existing requests will be allowed to finish.", usb);
	}
}
