package bitwise.devices.canon.requests;

import bitwise.devices.camera.LiveViewOnRequest;
import bitwise.devices.camera.LiveViewOnRequester;
import bitwise.devices.canon.BaseCanon;
import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.engine.service.RequestContext;

public class LiveViewOn extends BaseUsbPtpCameraRequest<BaseCanon, LiveViewOnRequester> implements LiveViewOnRequest {
	public LiveViewOn(BaseCanon in_service, LiveViewOnRequester in_requester) {
		super(in_service, in_requester);
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().setDevicePropValue((short)CanonDeviceProperties.EVFOutputDevice, new Int32(0x2));
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
