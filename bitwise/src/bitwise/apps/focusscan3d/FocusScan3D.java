package bitwise.apps.focusscan3d;

import javafx.application.Platform;
import javafx.stage.Stage;
import bitwise.apps.App;
import bitwise.apps.events.AppLaunchedEvent;
import bitwise.apps.focusscan3d.gui.camera.CameraView;
import bitwise.apps.focusscan3d.gui.deviceselect.DeviceSelectView;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.ReadyDevice;
import bitwise.devices.usb.UsbGetDriverRequest;
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
	public void handleEvent(Event in_event) throws InterruptedException {
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
	}
	
	private UsbGetDriverRequest<?, FullCamera> cameraRequest = null;
	private FullCamera driver = null;
	
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
		}
	}
	
	public void fx_setDriver(ReadyDevice<?> ready) {
		cameraRequest = getDriver(ready, FullCamera.class);
		Supervisor.getUSB().enqueueRequest(cameraRequest);
		hideDeviceSelect();
	}
	
	public void fx_deviceSelectViewClosed() {
		if (null == driver)
			terminate();
	}
	
	public void fx_cameraViewClosed() {
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
	
	private void showCameraView() {
		if (null == cameraStage) {
			FocusScan3D app = this;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cameraStage = new Stage();
					CameraView.showNewWindow(cameraStage, app);
				}
			});
		}
		else {
			cameraStage.show();
		}
	}
}
