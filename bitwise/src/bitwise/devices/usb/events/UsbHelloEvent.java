package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbService;
import bitwise.engine.eventbus.Event;


public final class UsbHelloEvent extends Event {
	private final UsbService usb;
	
	public UsbHelloEvent(UsbService in_usb) {
		super("USB Hello");
		usb = in_usb;
		assert(null != usb);
	}

	@Override
	public String getDescription() {
		return String.format("USB [%s] started and ready to process new requests.", usb);
	}
}
