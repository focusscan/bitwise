package bitwise.devices.usbservice.requests;

import bitwise.engine.service.BaseRequester;

public interface AddUsbDriverFactoryRequester extends BaseRequester {
	public void notifyRequestComplete(AddUsbDriverFactory<?> in);
}
