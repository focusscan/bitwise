package bitwise.apps.focusscan;

import java.util.List;

import bitwise.apps.BaseApp;
import bitwise.apps.focusscan.gui.DeviceSelect;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.devices.camera.*;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbServiceHandle;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.devices.usbservice.requests.StartUsbDriverRequester;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.supervisor.Supervisor;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class FocusScan extends BaseApp<FocusScanHandle> implements StartUsbDriverRequester, CameraListener, GetPropertyRequester, SetPropertyRequester {
	private final FocusScanHandle handle = new FocusScanHandle(this);
	private Stage stage = null;
	private CameraHandle cameraHandle = null;
	
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
	
	public CameraHandle getCameraHandle() {
		return cameraHandle;
	}
	
	public void selectDevice(UsbReady<?> ready) {
		UsbServiceHandle usbService = Supervisor.getInstance().getUsbServiceHandle();
		usbService.startUsbDriver(this, ready);
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
	
	private volatile ScanSetup scanSetup = null;
	
	public void fxdo_Hello(ScanSetup in) {
		scanSetup = in;
		
		cameraHandle.getBatteryLevel(this);
		cameraHandle.getExposureProgramMode(this);
		cameraHandle.getExposureTime(this);
		cameraHandle.getFlashMode(this);
		cameraHandle.getFNumber(this);
		cameraHandle.getFocalLength(this);
		cameraHandle.getFocusMode(this);
		cameraHandle.getIso(this);
		cameraHandle.getStorageDevices(this);
		cameraHandle.getImageFormats(this);
	}

	@Override
	public void notifyRequestComplete(StartUsbDriver<?> in) {
		cameraHandle = (CameraHandle) in.getHandle();
		cameraHandle.setCameraEventListener(this);
		
		final FocusScan thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ScanSetup.showScanSetup(thing, stage);
			}
		});
	}

	@Override
	public void notifyRequestComplete(GetPropertyRequest<?> in) {
		if (!in.gotValues()) {
			Log.log(this, "Failed to get values: %s", in);
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
		case ExposureProgramMode:
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setExposureProgramMode((ExposureProgramMode) in.getValue());
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
		case FlashMode:
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setFlashMode((FlashMode) in.getValue());
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
		case FocusMode:
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setFocusMode((FocusMode) in.getValue());
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
		case StorageDevices:
		{
			Platform.runLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setStorageDevices((List<StorageDevice>) in.getValue());
				}
			});
			break;
		}
		case ImageFormats:
		{
			Platform.runLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					if (null != scanSetup)
						scanSetup.setImageFormats((List<ImageFormat>) in.getValue());
				}
			});
			break;
		}
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
		case ExposureProgramMode:
			camera.getExposureProgramMode(this);
			break;
		case ExposureTime:
			camera.getExposureTime(this);
			break;
		case FlashMode:
			camera.getFlashMode(this);
			break;
		case FNumber:
			camera.getFNumber(this);
			break;
		case FocalLength:
			camera.getFocalLength(this);
			break;
		case FocusMode:
			camera.getFocusMode(this);
			break;
		case Iso:
			camera.getIso(this);
			break;
		case StorageDevices:
			camera.getStorageDevices(this);
			break;
		case ImageFormats:
			camera.getImageFormats(this);
			break;
		default:
			Log.log(this, "Property %s - %s changed, ignoring", camera, property);
		}
	}
}