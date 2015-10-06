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
	
	public ExposureMode getExposureMode();
	public int getExposureTime();
	public short getExposureIndex();
	
	public FocusMode getFocusMode();
	public FlashMode getFlashMode();
}
