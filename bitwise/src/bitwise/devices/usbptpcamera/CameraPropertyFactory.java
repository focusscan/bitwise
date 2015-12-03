package bitwise.devices.usbptpcamera;

import bitwise.devices.camera.*;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.coder.UsbPtpTypeCastException;

public interface CameraPropertyFactory {
	public BatteryLevel getBatteryLevel(UsbPtpPrimType in, UsbPtpPrimType min, UsbPtpPrimType max) throws UsbPtpTypeCastException;
	public FocalLength getFocalLength(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	
	public ExposureProgramMode getExposureProgramMode(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public ExposureTime getExposureTime(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public FlashMode getFlashMode(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public FNumber getFNumber(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public FocusMode getFocusMode(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public ImageFormat getImageFormat(short in) throws UsbPtpTypeCastException;
	public Iso getIso(UsbPtpPrimType in) throws UsbPtpTypeCastException;
	public StorageDevice getStorageDevice(BaseUsbPtpCamera.StorageIDwInfo in);
	public WhiteBalanceMode getWhiteBalanceMode(UsbPtpPrimType currentValue) throws UsbPtpTypeCastException;
}
