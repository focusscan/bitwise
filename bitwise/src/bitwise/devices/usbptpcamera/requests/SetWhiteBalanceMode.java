package bitwise.devices.usbptpcamera.requests;

import bitwise.devices.camera.CameraProperty;
import bitwise.devices.camera.WhiteBalanceMode;
import bitwise.devices.camera.SetPropertyRequest;
import bitwise.devices.camera.SetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.*;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.engine.service.RequestContext;

public class SetWhiteBalanceMode <A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, SetPropertyRequester> implements SetPropertyRequest<WhiteBalanceMode> {
	private final WhiteBalanceMode value;
	
	public SetWhiteBalanceMode(A in_service, SetPropertyRequester in_requester, WhiteBalanceMode in) {
		super(in_service, in_requester);
		value = in;
	}

	@Override
	public CameraProperty getProperty() {
		return CameraProperty.WhiteBalance;
	}

	@Override
	public WhiteBalanceMode getValue() {
		return value;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().setDevicePropValue(DevicePropCode.whiteBalance, new Int32(value.getValue()));
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
