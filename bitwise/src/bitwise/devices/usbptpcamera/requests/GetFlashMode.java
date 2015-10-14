package bitwise.devices.usbptpcamera.requests;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.camera.FlashMode;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.UsbPtpException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.devices.usbptpcamera.responses.DevicePropertyEnum;
import bitwise.engine.service.RequestContext;
import bitwise.log.Log;

public class GetFlashMode<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetPropertyRequester> implements GetPropertyRequest<FlashMode> {
	private final CameraPropertyFactory propertyFactory;
	
	public GetFlashMode(A in_service, GetPropertyRequester in_requester, CameraPropertyFactory in_propertyFactory) {
		super(in_service, in_requester);
		propertyFactory = in_propertyFactory;
	}
	
	private boolean success = false;
	private FlashMode value = null;
	private List<FlashMode> values = null;
	private boolean settable = false;
	
	@Override
	public boolean gotValues() {
		return success;
	}

	@Override
	public FlashMode getValue() {
		return value;
	}

	@Override
	public List<FlashMode> getLegalValues() {
		return values;
	}

	@Override
	public boolean canSet() {
		return settable;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		try {
			DevicePropDesc prop = getService().getDevicePropDesc(DevicePropCode.flashMode);
			if (null == prop)
				return;
			settable = prop.supportsSet();
			value = propertyFactory.getFlashMode(prop.currentValue);
			if (null != prop.form && prop.form instanceof DevicePropertyEnum) {
				DevicePropertyEnum enm = (DevicePropertyEnum) prop.form;
				values = new ArrayList<>(enm.supportedValues.length);
				for (UsbPtpPrimType sv : enm.supportedValues)
					values.add(propertyFactory.getFlashMode(sv));
			}
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
