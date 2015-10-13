package bitwise.devices.nikon;

import bitwise.devices.usbptpcamera.UsbPtpCameraRequest;
import bitwise.engine.service.BaseRequester;

public abstract class NikonCameraRequest<R extends BaseRequester> extends UsbPtpCameraRequest<NikonBase, R> {
	protected NikonCameraRequest(NikonBase in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
