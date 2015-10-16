package bitwise.apps.focusscan;

import java.util.List;

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

public class FocusScanStateScan extends FocusScanState {
	private final int steps;
	private final int stepsPerImage;
	private volatile Scan scan = null;
	
	public FocusScanStateScan(FocusScan in_app, int in_steps, int in_stepsPerImage) {
		super(in_app);
		steps = in_steps;
		stepsPerImage = in_stepsPerImage;
	}

	@Override
	public FocusScanState updateBatteryLevel(BatteryLevel in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocusScanState updateExposureTime(ExposureTime in, List<ExposureTime> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocusScanState updateFNumber(FNumber in, List<FNumber> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocusScanState updateFocalLength(FocalLength in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocusScanState updateIso(Iso in, List<Iso> values) {
		// TODO Auto-generated method stub
		return null;
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
		scan = in;
		// TODO: start scanning task
		return this;
	}

	@Override
	public FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException {
		return this;
	}

	@Override
	public FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException {
		return this;
	}

	@Override
	public FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException {
		return this;
	}
}
