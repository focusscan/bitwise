package bitwise.devices.usbptpcamera;

import bitwise.devices.BaseDriverRequest;
import bitwise.engine.service.BaseRequester;

public abstract class BaseUsbPtpCameraRequest<A extends BaseUsbPtpCamera<?>, R extends BaseRequester> extends BaseDriverRequest<A, R> {
	protected BaseUsbPtpCameraRequest(A in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
