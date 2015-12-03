package bitwise.devices.canon;

import bitwise.devices.camera.*;
import bitwise.devices.canon.requests.*;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraHandle;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;

public class CanonHandle extends BaseUsbPtpCameraHandle<BaseCanon> {
	private final CanonPropertyFactory propertyFactory = new CanonPropertyFactory();
	
	protected CanonHandle(BaseCanon in_service) {
		super(in_service);
	}
	
	@Override
	public CameraPropertyFactory getCameraPropertyFactory() {
		return propertyFactory;
	}

	@Override
	public LiveViewOnRequest liveViewOn(LiveViewOnRequester requester) {
		LiveViewOn r = new LiveViewOn(getService(), requester);
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public LiveViewOffRequest liveViewOff(LiveViewOffRequester requester) {
		LiveViewOff r = new LiveViewOff(getService(), requester);
		this.enqueueRequest(r);
		return r;
	}
	
	@Override
	public GetLiveViewImageRequest getLiveViewImage(GetLiveViewImageRequester requester) {
		GetLiveViewImage r = new GetLiveViewImage(getService(), requester);
		this.enqueueRequest(r);
		return r;
	}
	
	@Override
	public TakeImageLVRequest takeImageLV(TakeImageLVRequester requester) {
		TakeImageLV r = new TakeImageLV(getService(), requester);
		this.enqueueRequest(r);
		return r;
	}
	
	@Override
	public DriveFocusRequest driveFocus(DriveFocusRequester requester, DriveFocusRequest.Direction direction, int steps, boolean blocking) {
		DriveFocus r = new DriveFocus(getService(), requester, direction, steps, blocking);
		this.enqueueRequest(r);
		return r;
	}
}
