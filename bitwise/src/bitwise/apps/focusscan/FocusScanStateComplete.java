package bitwise.apps.focusscan;

import java.util.List;

import bitwise.apps.focusscan.gui.Completed;
import bitwise.apps.focusscan.gui.Scan;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.LiveViewOffRequest;
import bitwise.devices.camera.LiveViewOnRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.requests.StartUsbDriver;

public class FocusScanStateComplete extends FocusScanState {
	@SuppressWarnings("unused")
	private Completed completed = null;
	
	public FocusScanStateComplete(FocusScan in_app) {
		super(in_app);
	}

	@Override
	public FocusScanState updateBatteryLevel(BatteryLevel in) {
		return this;
	}

	@Override
	public FocusScanState updateExposureTime(ExposureTime in, List<ExposureTime> values) {
		return this;
	}

	@Override
	public FocusScanState updateFNumber(FNumber in, List<FNumber> values) {
		return this;
	}

	@Override
	public FocusScanState updateFocalLength(FocalLength in) {
		return this;
	}

	@Override
	public FocusScanState updateIso(Iso in, List<Iso> values) {
		return this;
	}

	@Override
	public FocusScanState selectDevice(UsbReady<?> ready) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState startUsbComplete(StartUsbDriver<?> in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState scanSetupHello(ScanSetup in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState takeTestImage() throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState startScan(int steps, int stepsPerImage) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState scanHello(Scan in) throws FocusScanException {
		throw new FocusScanException();
	}
	
	@Override
	public FocusScanState scanNewImage(byte[] lv, byte[] si) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState scanCancelled() throws FocusScanException {
		throw new FocusScanException();
	}
	
	@Override
	public FocusScanState scanComplete() throws FocusScanException {
		throw new FocusScanException();
	}
	
	@Override
	public FocusScanState completedHello(Completed in) throws FocusScanException {
		completed = in;
		getApp().cameraHandle.liveViewOff(getApp());
		return this;
	}

	@Override
	public FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException {
		throw new FocusScanException();
	}
}
