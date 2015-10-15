package bitwise.devices.nikon.requests;

import bitwise.devices.camera.LiveViewOnRequest;
import bitwise.devices.camera.LiveViewOnRequester;
import bitwise.devices.nikon.BaseNikon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class LiveViewOn extends BaseUsbPtpCameraRequest<BaseNikon, LiveViewOnRequester> implements LiveViewOnRequest {
	public LiveViewOn(BaseNikon in_service, LiveViewOnRequester in_requester) {
		super(in_service, in_requester);
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().startLiveView();
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
