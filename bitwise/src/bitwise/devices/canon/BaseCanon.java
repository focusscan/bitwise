package bitwise.devices.canon;

import java.util.Hashtable;

import bitwise.devices.canon.CanonHandle;
import bitwise.devices.canon.operations.*;
import bitwise.devices.canon.responses.*;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
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
			checkForCameraEvents();
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
	
	public boolean checkForCameraEvents() throws InterruptedException {
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
			if (null != curr) curr.update(props.get(key));
			else currentDeviceProperties.put(key, props.get(key));
		}
		return true;
	}
	
	@Override
	public DevicePropDesc getDevicePropDesc(short deviceProp) throws InterruptedException, UsbPtpCoderException {
		checkForCameraEvents();
		return currentDeviceProperties.get(deviceProp);
		
	}
	
	@Override
	public boolean setDevicePropValue(short deviceProp, UsbPtpPrimType value) throws InterruptedException {
		SetDevicePropValueEx request = new SetDevicePropValueEx(deviceProp, value);
		runOperation(request);
		checkForCameraEvents();
		return request.isSuccess();
	}
	
	public void getLiveViewImage(bitwise.devices.canon.requests.GetLiveViewImage getLiveViewImage) throws InterruptedException {

		checkForCameraEvents();
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
}
