package bitwise.devices.canon;

import bitwise.devices.camera.*;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.coder.Int8;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.coder.UsbPtpTypeCastException;

public class CanonPropertyFactory implements CameraPropertyFactory {
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
		return new FocalLength("--", 0);
	}
	
	@Override
	public ExposureProgramMode getExposureProgramMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		String label = CanonDeviceProperties.ExposureModeValues.get(raw.value);
		return new ExposureProgramMode(label, (short)raw.value);
	}
	
	@Override
	public ExposureTime getExposureTime(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		String label = CanonDeviceProperties.ShutterSpeedValues.get(raw.value);
		return new ExposureTime(label, raw.value);
	}
	
	@Override
	public FlashMode getFlashMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		return new FlashMode("--", (short)0);
	}
	
	@Override
	public FNumber getFNumber(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		String label = CanonDeviceProperties.ApertureValues.get(raw.value);
		return new FNumber(label, (short)raw.value);
	}
	
	@Override
	public FocusMode getFocusMode(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		String label = CanonDeviceProperties.FocusModeValues.get(raw.value);
		return new FocusMode(label, (short)raw.value);
	}
	
	@Override
	public ImageFormat getImageFormat(short in) throws UsbPtpTypeCastException {
		return new ImageFormat(String.format("%04x", in), in);
	}
	
	@Override
	public Iso getIso(UsbPtpPrimType in) throws UsbPtpTypeCastException {
		Int32 raw = in.castTo(Int32.class);
		String label = CanonDeviceProperties.ExposureIndexValues.get(raw.value);
		return new Iso(label, (short)raw.value);
	}
	
	@Override
	public StorageDevice getStorageDevice(BaseUsbPtpCamera.StorageIDwInfo in) {
		return new StorageDevice(in.info.volumeLabel, in.storageID);
	}
}
