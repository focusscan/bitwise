package bitwise.devices.usbptpcamera.requests;

import java.util.List;

import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.UsbPtpException;
import bitwise.devices.usbptpcamera.operations.DevicePropCode;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.devices.usbptpcamera.responses.DevicePropertyRange;
import bitwise.engine.service.RequestContext;
import bitwise.log.Log;

public class GetBatteryLevel<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetPropertyRequester> implements GetPropertyRequest<BatteryLevel> {
	private final CameraPropertyFactory propertyFactory;
	
	public GetBatteryLevel(A in_service, GetPropertyRequester in_requester, CameraPropertyFactory in_propertyFactory) {
		super(in_service, in_requester);
		propertyFactory = in_propertyFactory;
	}
	
	private boolean success = false;
	private BatteryLevel value = null;
	private boolean settable = false;
	
	@Override
	public boolean gotValues() {
		return success;
	}

	@Override
	public BatteryLevel getValue() {
		return value;
	}

	@Override
	public List<BatteryLevel> getLegalValues() {
		return null;
	}

	@Override
	public boolean canSet() {
		return settable;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		try {
			DevicePropDesc prop = getService().getDevicePropDesc(DevicePropCode.batteryLevel);
			if (null == prop)
				return;
			settable = prop.supportsSet();
			if (null != prop.form && prop.form instanceof DevicePropertyRange) {
				DevicePropertyRange range = (DevicePropertyRange) prop.form;
				value = propertyFactory.getBatteryLevel(prop.currentValue, range.minimumValue, range.maximumValue);				
				success = true;
			}
		} catch (UsbPtpException e) {
			Log.logException(this, e);
		}
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
