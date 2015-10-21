package bitwise.devices.usbservice.requests;

import bitwise.engine.service.BaseRequester;

public interface StartUsbDriverRequester extends BaseRequester {
	public void notifyRequestComplete(StartUsbDriver<?> in);
}
