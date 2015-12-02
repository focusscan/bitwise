package bitwise.devices.nikon;

import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.nikon.events.NikonEventCode;
import bitwise.devices.nikon.operations.*;
import bitwise.devices.nikon.reponses.LiveViewObject;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.operations.DeleteObject;
import bitwise.devices.usbptpcamera.operations.GetObject;
import bitwise.devices.usbptpcamera.operations.GetObjectInfo;
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
	
	public short checkDeviceReady() throws InterruptedException {
		DeviceReady request = new DeviceReady();
		runOperation(request);
		if (null == request.getResponseCode())
			return 0;
		return request.getResponseCode().getResponseCode();
	}
	
	public boolean startLiveView() throws InterruptedException {
		StartLiveView request = new StartLiveView();
		runOperation(request);
		deviceBusy();
		deviceBusy();
		while (deviceBusy())
			Thread.sleep(100);
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.success);
	}
	
	public boolean endLiveView() throws InterruptedException {
		EndLiveView request = new EndLiveView();
		runOperation(request);
		deviceBusy();
		deviceBusy();
		return (null != request.getResponseCode() && request.getResponseCode().getResponseCode() == ResponseCode.success);
	}
	
	public boolean driveFocus(DriveFocusRequest.Direction direction, int steps, boolean blocking) throws InterruptedException {
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
		
		if (!request.isSuccess())
			return false;
		
		final short responseCodeMfDriveEnd = (short) 0xa00c;
		
		int response = 0;
		boolean ret = true;
		boolean keepChecking = blocking;
		for (int tries = 0; (tries < 2) || keepChecking; tries++) {
			response = checkDeviceReady();
			ret = ret && (response != responseCodeMfDriveEnd);
			keepChecking = keepChecking && response == ResponseCode.deviceBusy;
			if (keepChecking)
				Thread.sleep(50);
		}
		
		return ret;
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
			Thread.sleep(250);
		boolean captureTerminated = false;
		while (!captureTerminated) {
			GetEvent getEvent = new GetEvent();
			runOperation(getEvent);
			if (!getEvent.isSuccess())
				return;
			try {
				for (Event e : getEvent.getDecodedData()) {
					{
						StringBuilder args = new StringBuilder();
						for (int arg : e.getArguments())
							args.append(String.format("%08x", arg));
						Log.log(this, "Event %04x %s", e.getEventCode(), args.toString());
					}
					if (NikonEventCode.objectAddedInSdram == e.getEventCode()) {
						int objectID = e.getArguments()[0];
						
						GetObjectInfo infoRequest = new GetObjectInfo(objectID);
						runOperation(infoRequest);
						if (!infoRequest.isSuccess())
							return;
						Log.log(this, "Capture added file %s", infoRequest.getDecodedData().filename);
						
						GetObject imageRequest = new GetObject(objectID);
						runOperation(imageRequest);
						if (!imageRequest.isSuccess())
							return;
						if (null != imageRequest.getResponseData() && 0 < imageRequest.getResponseData().getDataSize())
							r.setImage(imageRequest.getResponseData().getData().getRemainingArray());
						
						runOperation(new DeleteObject(objectID));
					}
					else if (NikonEventCode.captureCompleteRecInSdram == e.getEventCode()) {
						captureTerminated = true;
					}
					Thread.sleep(250);
				}
			} catch (UsbPtpCoderException e) {
				Log.logException(this, e);
			}
		}
	}
}
