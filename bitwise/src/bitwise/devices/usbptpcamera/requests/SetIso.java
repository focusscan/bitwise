package bitwise.devices.usbptpcamera.requests;

import bitwise.devices.camera.CameraProperty;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.SetPropertyRequest;
import bitwise.devices.camera.SetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.*;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.engine.service.RequestContext;

public class SetIso <A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, SetPropertyRequester> implements SetPropertyRequest<Iso> {
	private final Iso value;
	
	public SetIso(A in_service, SetPropertyRequester in_requester, Iso in) {
		super(in_service, in_requester);
		value = in;
	}

	@Override
	public CameraProperty getProperty() {
		return CameraProperty.FNumber;
	}

	@Override
	public Iso getValue() {
		return value;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().setDevicePropValue(DevicePropCode.exposureIndex, new Int16(value.getValue()));
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
