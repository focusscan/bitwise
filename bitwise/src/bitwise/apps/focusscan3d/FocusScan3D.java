package bitwise.apps.focusscan3d;

import javafx.application.Platform;
import javafx.stage.Stage;
import bitwise.apps.App;
import bitwise.apps.events.AppLaunchedEvent;
import bitwise.apps.focusscan3d.gui.camera.CameraView;
import bitwise.apps.focusscan3d.gui.deviceselect.DeviceSelectView;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.ReadyDevice;
import bitwise.engine.eventbus.Event;

public class FocusScan3D extends App {
	private FullCamera driver = null;
	
	public FocusScan3D() {
		super();
	}

	@Override
	public String getName() {
		return FocusScan3DFactory.driverName;
	}

	@Override
	public void handleEvent(Event event) throws InterruptedException {
		if (event instanceof AppLaunchedEvent) {
			AppLaunchedEvent launched = (AppLaunchedEvent)event;
			if (launched.getApp() == this)
				handleLaunched();
		}
	}
	
	private Stage deviceSelectStage = null;
	private Stage cameraStage = null;
	
	private void handleLaunched() {
		assert(null == driver);
		FocusScan3D app = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				deviceSelectStage = new Stage();
				DeviceSelectView.showNewWindow(deviceSelectStage, app);
			}
		});
	}
	
	public void fx_setDriver(ReadyDevice<?> ready) {
		assert(null == driver);
		assert(null != ready);
		FullCamera camera = getDriver(ready, FullCamera.class);
		if (null != camera) {
			driver = camera;
			deviceSelectStage.close();
			deviceSelectStage = null;
			cameraStage = new Stage();
			CameraView.showNewWindow(cameraStage, this);
		}
	}
	
	public void fx_deviceSelectViewClosed() {
		if (null == driver)
			terminate();
	}
	
	public void fx_cameraViewClosed() {
		terminate();
	}
}
