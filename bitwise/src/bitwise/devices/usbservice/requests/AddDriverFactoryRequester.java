package bitwise.devices.usbservice.requests;

import bitwise.engine.service.Requester;

public interface AddDriverFactoryRequester extends Requester {
	public void notifyRequestComplete(AddDriverFactory<?, ?, ?> in);
}
