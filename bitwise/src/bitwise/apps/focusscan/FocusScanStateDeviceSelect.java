package bitwise.apps.focusscan;

import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.LiveViewOffRequest;
import bitwise.devices.camera.LiveViewOnRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbServiceHandle;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.engine.supervisor.Supervisor;
import javafx.application.Platform;

public class FocusScanStateDeviceSelect extends FocusScanState {
	public FocusScanStateDeviceSelect(FocusScan in_app) {
		super(in_app);
	}
	
	@Override
	public FocusScanState selectDevice(UsbReady<?> ready) throws FocusScanException {
		UsbServiceHandle usbService = Supervisor.getInstance().getUsbServiceHandle();
		usbService.startUsbDriver(getApp(), ready);
		return this;
	}
	
	@Override
	public FocusScanState startUsbComplete(StartUsbDriver<?> in) throws FocusScanException {
		getApp().cameraHandle = (CameraHandle) in.getHandle();
		getApp().cameraHandle.setCameraEventListener(getApp());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ScanSetup.showScanSetup(getApp(), getApp().stage);
			}
		});
		return new FocusScanStateScanSetup(getApp());
	}

	@Override
	public ScanSetup getScanSetup() throws FocusScanException {
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
