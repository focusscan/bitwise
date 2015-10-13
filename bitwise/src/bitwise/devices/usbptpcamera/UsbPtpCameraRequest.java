package bitwise.devices.usbptpcamera;

import bitwise.devices.BaseDriverRequest;
import bitwise.engine.service.BaseRequester;

public abstract class UsbPtpCameraRequest<A extends UsbPtpCamera<?>, R extends BaseRequester> extends BaseDriverRequest<A, R> {
	protected UsbPtpCameraRequest(A in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
