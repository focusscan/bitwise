package bitwise.devices.usbptpcamera.requests;

import java.util.List;

import bitwise.devices.camera.CameraProperty;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.UsbPtpException;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.engine.service.RequestContext;
import bitwise.log.Log;

public class GetFocalLength<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetPropertyRequester> implements GetPropertyRequest<FocalLength> {
	private final CameraPropertyFactory propertyFactory;
	
	public GetFocalLength(A in_service, GetPropertyRequester in_requester, CameraPropertyFactory in_propertyFactory) {
		super(in_service, in_requester);
		propertyFactory = in_propertyFactory;
	}
	
	@Override
	public CameraProperty getProperty() {
		return CameraProperty.FocalLength;
	}
	
	private boolean success = false;
	private FocalLength value = null;
	private boolean settable = false;
	
	@Override
	public boolean gotValues() {
		return success;
	}

	@Override
	public FocalLength getValue() {
		return value;
	}

	@Override
	public List<FocalLength> getLegalValues() {
		return null;
	}

	@Override
	public boolean canSet() {
		return settable;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		try {
			DevicePropDesc prop = getService().getDevicePropDesc(DevicePropCode.focalLength);
			if (null == prop)
				return;
			settable = prop.supportsSet();
			value = propertyFactory.getFocalLength(prop.getCurrentValue());
			success = true;
		} catch (UsbPtpException e) {
			Log.logException(this, e);
		}
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
