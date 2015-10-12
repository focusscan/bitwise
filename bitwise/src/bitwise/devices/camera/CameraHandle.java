package bitwise.devices.camera;

import java.util.List;

import bitwise.devices.DriverRequest;

public interface CameraHandle<R extends DriverRequest> {
	public int getBatteryRemaining();
	
	public List<StorageDevice> getStorageDevices();
	public List<ImageFormat> getImageFormats();
	public R takePicture(StorageDevice in_storage, ImageFormat in_format);
	
	public FNumber getFNumber();
	public List<FNumber> getFNumbers();
	public R setFNumber(FNumber in);
	
	public ExposureTime getExposureTime();
	public List<ExposureTime> getExposureTimes();
	public R setExposureTime(ExposureTime in);
	
	public Iso getIso();
	public List<Iso> getIsos();
	public R setIso(Iso in);
	
	public ExposureMode getExposureMode();
	public List<ExposureMode> getExposureModes();
	public R setExposureMode(ExposureMode in);
	
	public FocusMode getFocusMode();
	public List<FocusMode> getFocusModes();
	public R setFocusMode(FocusMode in);
	
	public FlashMode getFlashMode();
	public List<FlashMode> getFlashModes();
	public R setFlashMode(FlashMode in);
}
