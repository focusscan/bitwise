package bitwise.devices.usbservice.requests;

import bitwise.engine.service.Requester;

public interface StartUsbDriverRequester extends Requester {
	public void notifyRequestComplete(StartUsbDriver<?, ?, ?> in);
}
