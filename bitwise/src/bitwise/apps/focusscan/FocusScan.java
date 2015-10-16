package bitwise.apps.focusscan;

import java.util.List;

import bitwise.apps.BaseApp;
import bitwise.apps.focusscan.gui.DeviceSelect;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.devices.camera.*;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.devices.usbservice.requests.StartUsbDriverRequester;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseServiceTask;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class FocusScan extends BaseApp<FocusScanHandle> implements StartUsbDriverRequester, CameraListener, GetPropertyRequester, SetPropertyRequester, LiveViewOnRequester, LiveViewOffRequester, GetLiveViewImageRequester, DriveFocusRequester, TakeImageLVRequester {
	private final FocusScanHandle handle = new FocusScanHandle(this);
	private FocusScanState state = new FocusScanStateDeviceSelect(this);
	protected Stage stage = null;
	protected CameraHandle cameraHandle = null;
	
	protected FocusScan() {
		super();
	}
	
	@Override
	public FocusScanHandle getServiceHandle() {
		return handle;
	}

	@Override
	protected boolean onStartService() {
		Log.log(this, "Focus Scan starting");
		final FocusScan thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage = new Stage();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						Log.log(thing, "Stage closed");
						stopServiceFromWithin();
					}
				});
				DeviceSelect.showDeviceSelect(thing, stage);
			}
		});
		return true;
	}

	@Override
	protected void onStopService() {
		Log.log(this, "Focus Scan stopped");
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
		Log.log(this, "Focus Scan request complete");
	}
	
	@Override
	public void notifyRequestComplete(GetPropertyRequest<?> in) {
		if (!in.gotValues()) {
			Log.log(this, "Failed to get values: %s", in);
			return;
		}
		ScanSetup scanSetup;
		try {
			scanSetup = state.getScanSetup();
		} catch (FocusScanException e) {
			Log.logException(this, e);
			return;
		}
		switch (in.getProperty()) {
		case BatteryLevel:
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setBatteryLevel((BatteryLevel) in.getValue());
				}
			});
			break;
		}
		case ExposureTime:
		{
			currentExposureTime = (ExposureTime) in.getValue();
			Platform.runLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setExposureTime((ExposureTime) in.getValue(), (List<ExposureTime>) in.getLegalValues());
				}
			});
			break;
		}
		case FNumber:
		{
			currentFNumber = (FNumber) in.getValue();
			Platform.runLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setFNumber((FNumber) in.getValue(), (List<FNumber>) in.getLegalValues());
				}
			});
			break;
		}
		case FocalLength:
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setFocalLength((FocalLength) in.getValue());
				}
			});
			break;
		}
		case Iso:
		{
			currentIso = (Iso) in.getValue();
			Platform.runLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setIso((Iso) in.getValue(), (List<Iso>) in.getLegalValues());
				}
			});
			break;
		}
		case ExposureProgramMode:
		case FlashMode:
		case FocusMode:
		case StorageDevices:
		case ImageFormats:
		default:
			Log.log(this, "I sent this request but I don't know what to do with it: %s", in);
		}
	}

	@Override
	public void onCameraPropertyChanged(CameraHandle camera, CameraProperty property) {
		switch (property) {
		case BatteryLevel:
			camera.getBatteryLevel(this);
			break;
		case ExposureTime:
			camera.getExposureTime(this);
			break;
		case FNumber:
			camera.getFNumber(this);
			break;
		case FocalLength:
			camera.getFocalLength(this);
			break;
		case Iso:
			camera.getIso(this);
			break;
		case ExposureProgramMode:
		case FlashMode:
		case FocusMode:
		case StorageDevices:
		case ImageFormats:
		default:
			Log.log(this, "Property %s - %s changed, ignoring", camera, property);
		}
	}
	
	private volatile ExposureTime currentExposureTime = null;
	private volatile FNumber currentFNumber = null;
	private volatile Iso currentIso = null;
	
	public void fxdo_setExposureTime(ExposureTime in) {
		if (null != cameraHandle && (null == currentExposureTime || !in.equals(currentExposureTime))) {
			cameraHandle.setExposureTime(this, in);
		}
	}
	
	public void fxdo_setFNumber(FNumber in) {
		if (null != cameraHandle && (null == currentFNumber || !in.equals(currentFNumber))) {
			cameraHandle.setFNumber(this, in);
		}
	}
	
	public void fxdo_setIso(Iso in) {
		if (null != cameraHandle && (null == currentIso || !in.equals(currentIso))) {
			cameraHandle.setIso(this, in);
		}
	}
	
	@Override
	public void notifyRequestComplete(SetPropertyRequest<?> in) {
		switch (in.getProperty()) {
		case ExposureTime:
			cameraHandle.getExposureTime(this);
			break;
		case FNumber:
			cameraHandle.getFNumber(this);
			break;
		case Iso:
			cameraHandle.getIso(this);
			break;
		default:
			Log.log(this, "I sent this request but I don't know what to do with it: %s", in);
		}
	}
	
	public void fxdo_selectDevice(UsbReady<?> ready) {
		try {
			state = state.selectDevice(ready);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	@Override
	public void notifyRequestComplete(StartUsbDriver<?> in) {
		try {
			state = state.startUsbComplete(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	public void fxdo_Hello(ScanSetup in) {
		try {
			state = state.scanSetupHello(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	protected void statedo_addServiceTask(BaseServiceTask<FocusScan> in) {
		this.addServiceTask(in);
	}
	
	@Override
	public void notifyRequestComplete(LiveViewOnRequest in) {
		try {
			state = state.notifyLiveViewOn(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	@Override
	public void notifyRequestComplete(LiveViewOffRequest in) {
		try {
			state = state.notifyLiveViewOff(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}

	@Override
	public void notifyRequestComplete(GetLiveViewImageRequest in) {
		try {
			state = state.notifyLiveViewImage(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	public void fxdo_takeTestImage() {
		try {
			state = state.takeTestImage();
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}
	
	@Override
	public void notifyRequestComplete(TakeImageLVRequest in) {
		try {
			state = state.notifyImage(in);
		} catch (FocusScanException e) {
			Log.logException(this, e);
		}
	}

	public void fxdo_focusNear(int steps) {
		cameraHandle.driveFocus(this, DriveFocusRequest.Direction.TowardsNear, steps);
	}
	
	public void fxdo_focusFar(int steps) {
		cameraHandle.driveFocus(this, DriveFocusRequest.Direction.TowardsFar, steps);
	}

	@Override
	public void notifyRequestComplete(DriveFocusRequest in) {
		// TODO Auto-generated method stub
		
	}

	public void fxdo_scanNearToFar(int steps, int stepsPerImage) {
		// TODO: kill the live view task,
		// switch to the scanning scene,
		// start the scanning task
	}
}
