package bitwise.apps.focusscan;

import bitwise.apps.BaseApp;
import bitwise.apps.focusscan.gui.DeviceSelect;
import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
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

public final class FocusScan extends BaseApp<FocusScanHandle> implements StartUsbDriverRequester, GetPropertyRequester {
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

	@Override
	public void notifyRequestComplete(StartUsbDriver<?> in) {
		cameraHandle = (CameraHandle) in.getHandle();
		cameraHandle.getBatteryLevel(this);
	}

	@Override
	public void notifyRequestComplete(GetPropertyRequest<?> in) {
		Log.log(this, "Property fetched: %s, value %s", in, in.getValue());
	}
}
