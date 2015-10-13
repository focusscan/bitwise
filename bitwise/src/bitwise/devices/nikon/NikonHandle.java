package bitwise.devices.nikon;

import java.util.List;

import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.CameraListener;
import bitwise.devices.camera.ExposureMode;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FlashMode;
import bitwise.devices.camera.FocusMode;
import bitwise.devices.camera.ImageFormat;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.StorageDevice;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraHandle;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;

public class NikonHandle extends BaseUsbPtpCameraHandle<BaseNikon> implements CameraHandle<BaseUsbPtpCameraRequest<BaseNikon, ?>> {
	protected NikonHandle(BaseNikon in_service) {
		super(in_service);
	}

	@Override
	public void setCameraEventListener(CameraListener in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBatteryRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<StorageDevice> getStorageDevices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ImageFormat> getImageFormats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> takePicture(StorageDevice in_storage, ImageFormat in_format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FNumber getFNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FNumber> getFNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setFNumber(FNumber in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExposureTime getExposureTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExposureTime> getExposureTimes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setExposureTime(ExposureTime in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iso getIso() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Iso> getIsos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setIso(Iso in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExposureMode getExposureMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExposureMode> getExposureModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setExposureMode(ExposureMode in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocusMode getFocusMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FocusMode> getFocusModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setFocusMode(FocusMode in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlashMode getFlashMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlashMode> getFlashModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseUsbPtpCameraRequest<BaseNikon, ?> setFlashMode(FlashMode in) {
		// TODO Auto-generated method stub
		return null;
	}
}
