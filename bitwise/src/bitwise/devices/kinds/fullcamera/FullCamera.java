package bitwise.devices.kinds.fullcamera;

import java.util.List;

import bitwise.devices.kinds.DeviceKind;
import bitwise.devices.usb.UsbRequest;

public interface FullCamera extends DeviceKind {
	public void fetchAllCameraProperties();
	
	public byte getBatteryLevel();
	
	public int getFocalLength();
	
	public FNumber getFNumber();
	public List<FNumber> getValidFNumbers();
	public UsbRequest setFNumber(FNumber in);
	
	public ExposureTime getExposureTime();
	public List<ExposureTime> getValidExposureTimes();
	public UsbRequest setExposureTime(ExposureTime in);
	
	public ExposureIndex getExposureIndex();
	public List<ExposureIndex> getValidExposureIndicies();
	public UsbRequest setExposureIndex(ExposureIndex in);
	
	public ExposureMode getExposureMode();
	public List<ExposureMode> getValidExposureModes();
	public UsbRequest setExposureMode(ExposureMode in);
	
	public FocusMode getFocusMode();
	public List<FocusMode> getValidFocusModes();
	public UsbRequest setFocusMode(FocusMode in);
	
	public FlashMode getFlashMode();
	public List<FlashMode> getValidFlashModes();
	public UsbRequest setFlashMode(FlashMode in);
}
