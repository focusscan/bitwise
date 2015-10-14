package bitwise.devices.usbptpcamera.requests;

import bitwise.devices.camera.CameraProperty;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.SetPropertyRequest;
import bitwise.devices.camera.SetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.*;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.engine.service.RequestContext;

public class SetExposureTime <A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, SetPropertyRequester> implements SetPropertyRequest<ExposureTime> {
	private final ExposureTime value;
	
	public SetExposureTime(A in_service, SetPropertyRequester in_requester, ExposureTime in) {
		super(in_service, in_requester);
		value = in;
	}

	@Override
	public CameraProperty getProperty() {
		return CameraProperty.ExposureTime;
	}

	@Override
	public ExposureTime getValue() {
		return value;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().setDevicePropValue(DevicePropCode.exposureTime, new Int32(value.getValue()));
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
