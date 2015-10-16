package bitwise.apps.focusscan;

import java.util.List;

import bitwise.apps.focusscan.gui.Completed;
import bitwise.apps.focusscan.gui.Scan;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.devices.camera.*;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.requests.StartUsbDriver;

public abstract class FocusScanState {
	private final FocusScan app;
	
	public FocusScanState(FocusScan in_app) {
		app = in_app;
	}
	
	protected final FocusScan getApp() {
		return app;
	}
	
	public abstract FocusScanState updateBatteryLevel(BatteryLevel in);
	public abstract FocusScanState updateExposureTime(ExposureTime in, List<ExposureTime> values);
	public abstract FocusScanState updateFNumber(FNumber in, List<FNumber> values);
	public abstract FocusScanState updateFocalLength(FocalLength in);
	public abstract FocusScanState updateIso(Iso in, List<Iso> values);
	
	public abstract FocusScanState selectDevice(UsbReady<?> ready) throws FocusScanException;
	public abstract FocusScanState startUsbComplete(StartUsbDriver<?> in) throws FocusScanException;
	
	public abstract FocusScanState scanSetupHello(ScanSetup in) throws FocusScanException;
	public abstract FocusScanState takeTestImage() throws FocusScanException;
	public abstract FocusScanState startScan(int steps, int stepsPerImage) throws FocusScanException;
	
	public abstract FocusScanState scanHello(Scan in) throws FocusScanException;
	public abstract FocusScanState scanNewImage(byte[] lv, byte[] si) throws FocusScanException;
	public abstract FocusScanState scanCancelled() throws FocusScanException;
	public abstract FocusScanState scanComplete() throws FocusScanException;
	
	public abstract FocusScanState completedHello(Completed in) throws FocusScanException;
	
	public abstract FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException;
	public abstract FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException;
	public abstract FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException;
	public abstract FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException;
}
