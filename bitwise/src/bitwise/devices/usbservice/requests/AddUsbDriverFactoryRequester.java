package bitwise.devices.usbservice.requests;

import bitwise.engine.service.Requester;

public interface AddUsbDriverFactoryRequester extends Requester {
	public void notifyRequestComplete(AddUsbDriverFactory<?, ?, ?> in);
}
