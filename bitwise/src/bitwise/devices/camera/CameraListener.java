package bitwise.devices.camera;

import java.util.List;

public interface CameraListener {
	public void onBatteryLevelChanged(int in);
	public void onFNumberChanged(FNumber in, List<FNumber> values);
	public void onExposureTimeChanged(ExposureTime in, List<ExposureTime> values);
	public void onIsoChanged(Iso in, List<Iso> values);
	public void onExposureModeChanged(ExposureMode in, List<ExposureMode> values);
	public void onFocusModeChanged(FocusMode in, List<FocusMode> values);
	public void onFlashModeChanged(FlashMode in, List<FlashMode> values);
}
