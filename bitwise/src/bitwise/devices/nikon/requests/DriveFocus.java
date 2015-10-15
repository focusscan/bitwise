package bitwise.devices.nikon.requests;

import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.camera.DriveFocusRequester;
import bitwise.devices.nikon.BaseNikon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class DriveFocus extends BaseUsbPtpCameraRequest<BaseNikon, DriveFocusRequester> implements DriveFocusRequest {
	private final DriveFocusRequest.Direction direction;
	private final int steps;
	
	public DriveFocus(BaseNikon in_service, DriveFocusRequester in_requester, DriveFocusRequest.Direction in_direction, int in_steps) {
		super(in_service, in_requester);
		direction = in_direction;
		steps = in_steps;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().driveFocus(direction, steps);
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
