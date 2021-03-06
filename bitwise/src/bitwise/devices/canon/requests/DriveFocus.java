package bitwise.devices.canon.requests;

import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.camera.DriveFocusRequester;
import bitwise.devices.canon.BaseCanon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class DriveFocus extends BaseUsbPtpCameraRequest<BaseCanon, DriveFocusRequester> implements DriveFocusRequest {
	private final DriveFocusRequest.Direction direction;
	private final int steps;
	private final boolean blocking;
	
	public DriveFocus(BaseCanon in_service, DriveFocusRequester in_requester, DriveFocusRequest.Direction in_direction, int in_steps, boolean in_blocking) {
		super(in_service, in_requester);
		direction = in_direction;
		steps = in_steps;
		blocking = in_blocking;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().driveFocus(direction, steps, blocking);
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}

	@Override
	public Result getDriveFocusResult() {
		return Result.FocusMoved;		// Canon doesn't report whether the focus actually moved or not
	}
}
