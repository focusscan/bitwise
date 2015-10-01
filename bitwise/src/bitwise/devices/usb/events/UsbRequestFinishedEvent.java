package bitwise.devices.usb.events;

import bitwise.devices.usb.UsbRequest;
import bitwise.engine.eventbus.Event;


public final class UsbRequestFinishedEvent extends Event {
	private final UsbRequest req;
	
	public UsbRequestFinishedEvent(UsbRequest in_req) {
		super("USB Request Finished");
		req = in_req;
		assert(null != req);
	}
	
	public UsbRequest getRequest() {
		return req;
	}

	@Override
	public String getDescription() {
		return String.format("USB Request (%s) finished.", req);
	}
}
