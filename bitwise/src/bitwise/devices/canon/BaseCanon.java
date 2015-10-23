package bitwise.devices.canon;

import java.util.Hashtable;

import bitwise.devices.canon.CanonHandle;
import bitwise.devices.canon.operations.*;
import bitwise.devices.canon.responses.*;
import bitwise.devices.nikon.operations.InitiateCaptureLV;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.coder.UsbPtpTypeCastException;
import bitwise.devices.usbptpcamera.operations.GetObject;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.devices.usbptpcamera.responses.DevicePropertyEnum;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.log.Log;

public abstract class BaseCanon extends BaseUsbPtpCamera<CanonHandle> {
	private static final byte interfaceNum = (byte)0x00;
	private static final byte dataInNum = (byte)0x02;
	private static final byte dataOutNum = (byte)0x81;
	private static final byte intrptNum = (byte)0x83;
	
	private Hashtable<Short, CanonDevicePropertyDesc> currentDeviceProperties = new Hashtable<>();
	
	private final CanonHandle handle;
	
	protected BaseCanon(UsbDevice in_device) {
		super(in_device, interfaceNum, dataInNum, dataOutNum, intrptNum);
		handle = new CanonHandle(this);
	}
	
	@Override
	protected boolean onStartPtpDriver() {
		SetRemoteMode setRemoteMode = new SetRemoteMode();
		SetEventMode setEventMode = new SetEventMode();
	
		try {
			runOperation(setRemoteMode);
			runOperation(setEventMode);
			checkForCameraEvents(null);
			return true;
		} catch (InterruptedException e) {
			Log.logException(this, e);
		}				
		return false;
	}
	
	@Override
	protected void onStopPtpDriver() {
		// TODO shutdown camera events
	}
	
	@Override
	public CanonHandle getServiceHandle() {
		return handle;
	}
	
	public boolean checkForCameraEvents(bitwise.devices.canon.requests.TakeImageLV r) throws InterruptedException {
		GetEvent request = new GetEvent();
		runOperation(request);
		
		Hashtable<Short, CanonDevicePropertyDesc> props = null;
		try {
			props = request.getDecodedData();
		} catch (UsbPtpCoderException e) {
			Log.logException(this, e);
		}
		
		if (null == props || props.isEmpty()) return false;
		
		for (short key : props.keySet()) {
			CanonDevicePropertyDesc curr = currentDeviceProperties.get(key);
			if (key == CanonDeviceProperties.NewObjectReady) {
				try {
					int objectID = ((DevicePropertyEnum)curr.getValidValues()).supportedValues[0].castTo(Int32.class).value;
					int objectSize = ((DevicePropertyEnum)curr.getValidValues()).supportedValues[3].castTo(Int32.class).value;
					GetPartialObject imageRequest = new GetPartialObject(objectID, objectSize);
					runOperation(imageRequest);
					
					if (null != imageRequest.getResponseData() && 0 < imageRequest.getResponseData().getDataSize())
						r.setImage(imageRequest.getResponseData().getData().getRemainingArray());
					return true;
					
				} catch (UsbPtpTypeCastException | UsbPtpCoderException e) {
					Log.logException(this, e);
					return false;
				}
						
				
			} else {
				if (null != curr) curr.update(props.get(key));
				else currentDeviceProperties.put(key, props.get(key));			
			}
		}
		return true;
	}
	
	@Override
	public DevicePropDesc getDevicePropDesc(short deviceProp) throws InterruptedException, UsbPtpCoderException {
		checkForCameraEvents(null);
		return currentDeviceProperties.get(deviceProp);
		
	}
	
	@Override
	public boolean setDevicePropValue(short deviceProp, UsbPtpPrimType value) throws InterruptedException {
		SetDevicePropValueEx request = new SetDevicePropValueEx(deviceProp, value);
		runOperation(request);
		Thread.sleep(100);
		checkForCameraEvents(null);
		return request.isSuccess();
	}
	
	public void getLiveViewImage(bitwise.devices.canon.requests.GetLiveViewImage getLiveViewImage) throws InterruptedException {

		checkForCameraEvents(null);
		GetLiveViewImage request = new GetLiveViewImage();
		runOperation(request);
		if (request.getResponseCode().getResponseCode() == ResponseCode.success) {
			try {
				LiveViewObject image = request.getDecodedData();
				getLiveViewImage.setImage(image.jpeg);
			} catch (UsbPtpCoderException e) {
				Log.logException(this, e);
			}
		}
	}
	
	public void takeImageLV(bitwise.devices.canon.requests.TakeImageLV r) throws InterruptedException {
		StartImageCapture requestHalfPress = new StartImageCapture(CanonDeviceProperties.ShutterState.HalfPress);
		StartImageCapture requestFullPress = new StartImageCapture(CanonDeviceProperties.ShutterState.FullPress);
		runOperation(requestHalfPress);
		if (!requestHalfPress.isSuccess()) return;
		runOperation(requestFullPress);
		if (!requestFullPress.isSuccess()) return;
		
		StopImageCapture requestFullRelease = new StopImageCapture(CanonDeviceProperties.ShutterState.FullPress);		
		StopImageCapture requestHalfRelease = new StopImageCapture(CanonDeviceProperties.ShutterState.HalfPress);
		runOperation(requestFullRelease);
		if (!requestFullRelease.isSuccess()) return;
		runOperation(requestHalfRelease);
		if (!requestHalfRelease.isSuccess()) return;
		
		while (!checkForCameraEvents(r))
			Thread.sleep(100);
	}
}
