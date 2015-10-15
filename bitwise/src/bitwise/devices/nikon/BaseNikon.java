package bitwise.devices.nikon;

import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.nikon.operations.*;
import bitwise.devices.nikon.reponses.LiveViewObject;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.operations.GetObject;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.log.Log;

public abstract class BaseNikon extends BaseUsbPtpCamera<NikonHandle> {
	private static final byte interfaceNum = (byte)0x00;
	private static final byte dataInNum = (byte)0x02;
	private static final byte dataOutNum = (byte)0x81;
	private static final byte intrptNum = (byte)0x83;
	
	private final NikonHandle handle;
	
	protected BaseNikon(UsbDevice in_device) {
		super(in_device, interfaceNum, dataInNum, dataOutNum, intrptNum);
		handle = new NikonHandle(this);
	}

	@Override
	protected boolean onStartPtpDriver() {
		return true;
	}

	@Override
	protected void onStopPtpDriver() {
	}

	@Override
	public NikonHandle getServiceHandle() {
		return handle;
	}
	
	public boolean deviceBusy() throws InterruptedException {
		DeviceReady request = new DeviceReady();
		runOperation(request);
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.deviceBusy);
	}
	
	public boolean startLiveView() throws InterruptedException {
		StartLiveView request = new StartLiveView();
		runOperation(request);
		deviceBusy();
		deviceBusy();
		while (deviceBusy())
			Thread.sleep(10);
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.success);
	}
	
	public boolean endLiveView() throws InterruptedException {
		EndLiveView request = new EndLiveView();
		runOperation(request);
		deviceBusy();
		deviceBusy();
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.success);
	}
	
	public boolean driveFocus(DriveFocusRequest.Direction direction, int steps) throws InterruptedException {
		int idirection;
		switch (direction) {
		case TowardsFar:
			idirection = 2;
			break;
		case TowardsNear:
		default:
			idirection = 1;
			break;
		}
		FocusDrive request = new FocusDrive(idirection, steps);
		runOperation(request);
		deviceBusy();
		deviceBusy();
		while (deviceBusy())
			Thread.sleep(10);
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.success);
	}
	
	public void getLiveViewImage(bitwise.devices.nikon.requests.GetLiveViewImage r) throws InterruptedException {
		GetLiveViewImage request = new GetLiveViewImage();
		runOperation(request);
		if (request.getResponseCode().getResponseCode() == ResponseCode.success) {
			try {
				LiveViewObject image = request.getDecodedData();
				r.setImage(image.jpeg);
			} catch (UsbPtpCoderException e) {
				Log.logException(this, e);
			}
		}
	}
	
	public void takeImageLV(bitwise.devices.nikon.requests.TakeImageLV r) throws InterruptedException {
		InitiateCaptureLV request = new InitiateCaptureLV();
		runOperation(request);
		if (!request.isSuccess())
			return;
		deviceBusy();
		deviceBusy();
		while (deviceBusy())
			Thread.sleep(10);
		try {
			if (0 < request.getObjectIDs().size()) {
				int objectID = request.getObjectIDs().get(0).value;
				GetObject imageRequest = new GetObject(objectID);
				runOperation(imageRequest);
				if (null != imageRequest.getResponseData() && 0 < imageRequest.getResponseData().getDataSize())
					r.setImage(imageRequest.getResponseData().getData().getRemainingArray());
			}
		} catch (UsbPtpCoderException e) {
			Log.logException(this, e);
		}
	}
}
