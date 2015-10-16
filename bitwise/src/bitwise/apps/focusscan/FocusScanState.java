package bitwise.apps.focusscan;

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
	
	public abstract ScanSetup getScanSetup() throws FocusScanException;
	
	public abstract FocusScanState selectDevice(UsbReady<?> ready) throws FocusScanException;
	public abstract FocusScanState startUsbComplete(StartUsbDriver<?> in) throws FocusScanException;
	
	public abstract FocusScanState scanSetupHello(ScanSetup in) throws FocusScanException;
	public abstract FocusScanState takeTestImage() throws FocusScanException;
	
	public abstract FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException;
	public abstract FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException;
	public abstract FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException;
	public abstract FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException;
}
