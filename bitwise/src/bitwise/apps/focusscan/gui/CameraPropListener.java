package bitwise.apps.focusscan.gui;

import java.util.List;

import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.Iso;

public interface CameraPropListener {
	public void updateBatteryLevel(BatteryLevel in);
	public void updateExposureTime(ExposureTime in, List<ExposureTime> values);
	public void updateFNumber(FNumber in, List<FNumber> values);
	public void updateFocalLength(FocalLength in);
	public void updateIso(Iso in, List<Iso> values);
}
