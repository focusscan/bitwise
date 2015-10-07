package bitwise.apps.focusscan3d;

import javafx.application.Platform;
import javafx.stage.Stage;
import bitwise.apps.App;
import bitwise.apps.events.AppLaunchedEvent;
import bitwise.apps.focusscan3d.gui.camera.CameraView;
import bitwise.apps.focusscan3d.gui.deviceselect.DeviceSelectView;
import bitwise.devices.kinds.fullcamera.*;
import bitwise.devices.kinds.fullcamera.events.*;
import bitwise.devices.usb.ReadyDevice;
import bitwise.devices.usb.drivers.UsbGetDriverRequest;
import bitwise.devices.usb.events.UsbRequestFinishedEvent;
import bitwise.engine.eventbus.Event;
import bitwise.engine.supervisor.Supervisor;

public class FocusScan3D extends App {
	public FocusScan3D() {
		super();
	}

	@Override
	public String getName() {
		return FocusScan3DFactory.driverName;
	}

	@Override
	public synchronized void handleEvent(Event in_event) throws InterruptedException {
		if (in_event instanceof AppLaunchedEvent) {
			AppLaunchedEvent event = (AppLaunchedEvent)in_event;
			if (event.getApp() == this)
				handleLaunched();
		}
		else if (in_event instanceof UsbRequestFinishedEvent) {
			UsbRequestFinishedEvent event = (UsbRequestFinishedEvent)in_event;
			if (event.getRequest().equals(cameraRequest))
				handleCameraSelected();
		}
		else if (in_event instanceof FocalLengthChanged) {
			FocalLengthChanged event = (FocalLengthChanged) in_event;
			if (event.getDriver() == driver)
				handleFocalLengthChangedEvent(event);
		}
		else if (in_event instanceof ExposureModeChanged) {
			ExposureModeChanged event = (ExposureModeChanged) in_event;
			if (event.getDriver() == driver)
				handleExposureModeChangedEvent(event);
		}
		else if (in_event instanceof FlashModeChanged) {
			FlashModeChanged event = (FlashModeChanged) in_event;
			if (event.getDriver() == driver)
				handleFlashModeChangedEvent(event);
		}
		else if (in_event instanceof FocusModeChanged) {
			FocusModeChanged event = (FocusModeChanged) in_event;
			if (event.getDriver() == driver)
				handleFocusModeChangedEvent(event);
		}
		else if (in_event instanceof FNumberChanged) {
			FNumberChanged event = (FNumberChanged) in_event;
			if (event.getDriver() == driver)
				handleFNumberChangedEvent(event);
		}
		else if (in_event instanceof ExposureTimeChanged) {
			ExposureTimeChanged event = (ExposureTimeChanged) in_event;
			if (event.getDriver() == driver)
				handleExposureTimeChangedEvent(event);
		}
		else if (in_event instanceof ExposureIndexChanged) {
			ExposureIndexChanged event = (ExposureIndexChanged) in_event;
			if (event.getDriver() == driver)
				handleExposureIndexChangedEvent(event);
		}
	}
	
	private void handleFocalLengthChangedEvent(FocalLengthChanged event) {
		if (null != cameraView) {
			int whole = event.getFocalLength() / 100;
			int part  = event.getFocalLength() % 100;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getFocalLengthProperty().set(String.format("%s.%smm", whole, part));
				}
			});
		}
	}
	
	private void handleExposureModeChangedEvent(ExposureModeChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getExposureModeValues().clear();
					cameraView.getExposureModeValues().addAll(event.getValidExposureModes());
					cameraView.getExposureModeValue().set(event.getExposureMode());
				}
			});
		}
	}

	private void handleFlashModeChangedEvent(FlashModeChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getFlashModeValues().clear();
					cameraView.getFlashModeValues().addAll(event.getValidFlashModes());
					cameraView.getFlashModeValue().set(event.getFlashMode());
				}
			});
		}
	}

	private void handleFocusModeChangedEvent(FocusModeChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getFocusModeValues().clear();
					cameraView.getFocusModeValues().addAll(event.getValidFocusModes());
					cameraView.getFocusModeValue().set(event.getFocusMode());
				}
			});
		}
	}
	
	private void handleFNumberChangedEvent(FNumberChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getApertureValues().clear();
					cameraView.getApertureValues().addAll(event.getValidFNumbers());
					cameraView.getApertureValue().set(event.getFNumber());
				}
			});
		}
	}

	private void handleExposureTimeChangedEvent(ExposureTimeChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getExposureTimeValues().clear();
					cameraView.getExposureTimeValues().addAll(event.getValidExposureTimes());
					cameraView.getExposureTimeValue().set(event.getExposureTime());
				}
			});
		}
	}
	
	private void handleExposureIndexChangedEvent(ExposureIndexChanged event) {
		if (null != cameraView) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraView.getExposureIndexValues().clear();
					cameraView.getExposureIndexValues().addAll(event.getValidExposureIndicies());
					cameraView.getExposureIndexValue().set(event.getExposureIndex());
				}
			});
		}
	}
	
	private UsbGetDriverRequest<?, FullCamera> cameraRequest = null;
	private FullCamera driver = null;
	
	public FullCamera getDriver() {
		return driver;
	}
	
	private void handleLaunched() {
		showDeviceSelect();
	}
	
	private void handleCameraSelected() {
		driver = cameraRequest.getDriverAsKind();
		if (null == driver) {
			cameraRequest = null;
			showDeviceSelect();
		}
		else {
			hideDeviceSelect();
			showCameraView();
			driver.fetchAllCameraProperties();
		}
	}
	
	public synchronized void fx_setDriver(ReadyDevice<?> ready) {
		cameraRequest = getDriverRequest(ready, FullCamera.class);
		Supervisor.getUSB().enqueueRequest(cameraRequest);
		hideDeviceSelect();
	}
	
	public synchronized void fx_deviceSelectViewClosed() {
		if (null == cameraRequest)
			terminate();
	}
	
	public synchronized void fx_cameraViewClosed() {
		terminate();
	}
	
	private Stage deviceSelectStage = null;
	
	private void showDeviceSelect() {
		if (null == deviceSelectStage) {
			FocusScan3D app = this;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					deviceSelectStage = new Stage();
					DeviceSelectView.showNewWindow(deviceSelectStage, app);
				}
			});
		}
		else {
			deviceSelectStage.show();
		}
	}
	
	private void hideDeviceSelect() {
		if (null != deviceSelectStage) {
			deviceSelectStage.hide();
			deviceSelectStage = null;
		}
	}
	
	private Stage cameraStage = null;
	private CameraView cameraView = null;
	
	private void showCameraView() {
		if (null == cameraStage) {
			FocusScan3D app = this;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraStage = new Stage();
					cameraView = CameraView.showNewWindow(cameraStage, app);
				}
			});
		}
		else {
			cameraStage.show();
		}
	}
	
	public void fx_setExposureMode(ExposureMode in) {
		if (!in.equals(driver.getExposureMode()))
			Supervisor.getUSB().enqueueRequest(driver.setExposureMode(in));
	}
	
	public void fx_setFlashMode(FlashMode in) {
		if (!in.equals(driver.getFlashMode()))
			Supervisor.getUSB().enqueueRequest(driver.setFlashMode(in));
	}
	
	public void fx_setFocusMode(FocusMode in) {
		if (!in.equals(driver.getFocusMode()))
			Supervisor.getUSB().enqueueRequest(driver.setFocusMode(in));
	}
	
	public void fx_setAperture(FNumber in) {
		if (!in.equals(driver.getFNumber()))
			Supervisor.getUSB().enqueueRequest(driver.setFNumber(in));
	}
	
	public void fx_setExposureTime(ExposureTime in) {
		if (!in.equals(driver.getExposureTime()))
			Supervisor.getUSB().enqueueRequest(driver.setExposureTime(in));
	}
	
	public void fx_setExposureIndex(ExposureIndex in) {
		if (!in.equals(driver.getExposureIndex()))
			Supervisor.getUSB().enqueueRequest(driver.setExposureIndex(in));
	}
}
