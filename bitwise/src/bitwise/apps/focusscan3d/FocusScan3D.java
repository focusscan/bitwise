package bitwise.apps.focusscan3d;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import bitwise.apps.App;
import bitwise.apps.events.AppLaunchedEvent;
import bitwise.apps.focusscan3d.gui.camera.CameraView;
import bitwise.apps.focusscan3d.gui.deviceselect.DeviceSelectView;
import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.kinds.fullcamera.events.FNumberChanged;
import bitwise.devices.kinds.fullcamera.events.FocalLengthChanged;
import bitwise.devices.usb.ReadyDevice;
import bitwise.devices.usb.UsbRequest;
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
		else if (in_event instanceof FNumberChanged) {
			FNumberChanged event = (FNumberChanged) in_event;
			if (event.getDriver() == driver)
				handleFNumberChangedEvent(event);
		}
	}
	
	private void handleFocalLengthChangedEvent(FocalLengthChanged event) {
		if (null != focalLength) {
			int whole = event.getFocalLength() / 100;
			int part  = event.getFocalLength() % 100;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					focalLength.set(String.format("%s.%smm", whole, part));
				}
			});
		}
	}
	
	private void handleFNumberChangedEvent(FNumberChanged event) {
		if (null != apertureValues && null != apertureValue) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					apertureValues.clear();
					apertureValues.addAll(event.getValidFNumbers());
					apertureValue.set(event.getFNumber());
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
	private StringProperty focalLength = null;
	private ObservableList<FNumber> apertureValues = null;
	private ObjectProperty<FNumber> apertureValue = null;
	
	private void showCameraView() {
		if (null == cameraStage) {
			FocusScan3D app = this;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraStage = new Stage();
					CameraView view = CameraView.showNewWindow(cameraStage, app);
					focalLength = view.getFocalLengthProperty();
					apertureValues = view.getApertureValues();
					apertureValue = view.getApertureValue();
				}
			});
		}
		else {
			cameraStage.show();
		}
	}
	
	public void fx_setAperture(FNumber in) {
		UsbRequest request = driver.setFNumber(in);
		Supervisor.getUSB().enqueueRequest(request);
	}
}
