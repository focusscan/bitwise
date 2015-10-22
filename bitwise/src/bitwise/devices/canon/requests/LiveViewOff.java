package bitwise.devices.canon.requests;

import bitwise.devices.camera.LiveViewOffRequest;
import bitwise.devices.camera.LiveViewOffRequester;
import bitwise.devices.canon.BaseCanon;
import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.engine.service.RequestContext;

public class LiveViewOff extends BaseUsbPtpCameraRequest<BaseCanon, LiveViewOffRequester> implements LiveViewOffRequest {
	public LiveViewOff(BaseCanon in_service, LiveViewOffRequester in_requester) {
		super(in_service, in_requester);
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().setDevicePropValue((short)CanonDeviceProperties.EVFOutputDevice, new Int32(0x1));
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
