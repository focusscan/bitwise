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
	public BatteryLevel getBatteryLevel(UsbPtpPrimType in, UsbPtpPrimType min, UsbPtpPrimType max) throws UsbPtpTypeCastException {
		Int8 raw = in.castTo(Int8.class);
		Int8 rawMin = min.castTo(Int8.class);
		Int8 rawMax = max.castTo(Int8.class);
		int pcnt = ((0xff & (int) raw.value) - (0xff & (int) rawMin.value)) * 100;
		pcnt = pcnt / ((0xff & (int) rawMax.value) - (0xff & (int) rawMin.value));
		return new BatteryLevel(String.format("%d%%", pcnt), (byte) pcnt);
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
		Int32 raw = in.castTo(Int32.class);
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
	public ImageFormat getImageFormat(short in) throws UsbPtpTypeCastException {
		return new ImageFormat(String.format("%04x", in), in);
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
