package bitwise.devices.usbptpcamera.requests;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.camera.ImageFormat;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.UsbPtpException;
import bitwise.devices.usbptpcamera.operations.DeviceInfo;
import bitwise.engine.service.RequestContext;
import bitwise.log.Log;

public class GetImageFormats<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetPropertyRequester> implements GetPropertyRequest<List<ImageFormat>> {
	private final CameraPropertyFactory propertyFactory;
	
	public GetImageFormats(A in_service, GetPropertyRequester in_requester, CameraPropertyFactory in_propertyFactory) {
		super(in_service, in_requester);
		propertyFactory = in_propertyFactory;
	}

	private boolean success = false;
	private List<ImageFormat> value = null;
	
	@Override
	public boolean gotValues() {
		return success;
	}

	@Override
	public List<ImageFormat> getValue() {
		return value;
	}

	@Override
	public List<List<ImageFormat>> getLegalValues() {
		return null;
	}

	@Override
	public boolean canSet() {
		return false;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		try {
			DeviceInfo deviceInfo = getService().getDeviceInfo();
			if (null != deviceInfo) {
				short[] imageFormats = deviceInfo.imageFormats;
				value = new ArrayList<>(imageFormats.length);
				for (short format : imageFormats)
					value.add(propertyFactory.getImageFormat(format));
				success = true;
			}
		} catch (UsbPtpException e) {
			Log.logException(this, e);
		}
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
}
