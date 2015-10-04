package bitwise.devices.usb.events;

import bitwise.apps.events.RequestFinishedEvent;
import bitwise.devices.usb.UsbRequest;


public final class UsbRequestFinishedEvent extends RequestFinishedEvent<UsbRequest> {
	public UsbRequestFinishedEvent(UsbRequest in_req) {
		super("USB Request Finished", in_req);
	}

	@Override
	public String getDescription() {
		return String.format("USB Request (%s) finished.", getRequest());
	}
}
