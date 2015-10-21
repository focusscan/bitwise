package bitwise.devices.usbservice;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class UsbServiceRequest<R extends BaseRequester> extends BaseRequest<UsbService, R> {
	protected UsbServiceRequest(UsbService in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
