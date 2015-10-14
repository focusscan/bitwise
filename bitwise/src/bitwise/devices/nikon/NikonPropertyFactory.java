package bitwise.devices.nikon;

import bitwise.devices.camera.*;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.coder.Int16;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.coder.Int8;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.coder.UsbPtpTypeCastException;

public class NikonPropertyFactory implements CameraPropertyFactory {
	@Override
	public BatteryLevel getBatteryLevel(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int8 raw = in.castTo(Int8.class);
		return new BatteryLevel(raw.toString(), raw.value);
	}
	
	@Override
	public FocalLength getFocalLength(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		return new FocalLength(raw.toString(), raw.value);
	}
	
	@Override
	public ExposureProgramMode getExposureProgramMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new ExposureProgramMode(raw.toString(), raw.value);
	}
	
	@Override
	public ExposureTime getExposureTime(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new ExposureTime(raw.toString(), raw.value);
	}
	
	@Override
	public FlashMode getFlashMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new FlashMode(raw.toString(), raw.value);
	}
	
	@Override
	public FNumber getFNumber(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new FNumber(raw.toString(), raw.value);
	}
	
	@Override
	public FocusMode getFocusMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new FocusMode(raw.toString(), raw.value);
	}
	
	@Override
	public ImageFormat getImageFormat(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new ImageFormat(raw.toString(), raw.value);
	}
	
	@Override
	public Iso getIso(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int16 raw = in.castTo(Int16.class);
		return new Iso(raw.toString(), raw.value);
	}
	
	@Override
	public StorageDevice getStorageDevice(BaseUsbPtpCamera.StorageIDwInfo in) {
		return new StorageDevice(in.info.volumeLabel, in.storageID);
	}
}
