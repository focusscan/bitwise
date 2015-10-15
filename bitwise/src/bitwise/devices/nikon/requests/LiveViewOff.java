package bitwise.devices.nikon.requests;

import bitwise.devices.camera.LiveViewOffRequest;
import bitwise.devices.camera.LiveViewOffRequester;
import bitwise.devices.nikon.BaseNikon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class LiveViewOff extends BaseUsbPtpCameraRequest<BaseNikon, LiveViewOffRequester> implements LiveViewOffRequest {
	public LiveViewOff(BaseNikon in_service, LiveViewOffRequester in_requester) {
		super(in_service, in_requester);
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().endLiveView();
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
