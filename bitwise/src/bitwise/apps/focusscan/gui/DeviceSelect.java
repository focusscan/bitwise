package bitwise.apps.focusscan.gui;

import java.io.IOException;
import java.util.List;

import bitwise.apps.focusscan.FocusScan;
import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.WhiteBalanceMode;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbServiceHandle;
import bitwise.engine.supervisor.Supervisor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DeviceSelect extends BorderPane implements CameraPropListener {
	public static DeviceSelect showDeviceSelect(FocusScan app, Stage primaryStage) {
		try {
			DeviceSelect view = new DeviceSelect(app);
			Scene scene = new Scene(view);
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Focus Scan - Select Device");
	        primaryStage.show();
	        return view;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML private ListView<UsbReady<?>> deviceList;
	@FXML private Button btnUseDevice;
	private final FocusScan app;
	
	private DeviceSelect(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeviceSelect.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		UsbServiceHandle usbService = Supervisor.getInstance().getUsbServiceHandle();
		deviceList.setItems(usbService.getReadyByHandleType(CameraHandle.class));
	}
	
	@FXML private void handleUseDevice(ActionEvent event) {
		UsbReady<?> ready = deviceList.getSelectionModel().getSelectedItem();
		if (null != ready)
			app.fxdo_selectDevice(ready);
	}

	@Override
	public void updateBatteryLevel(BatteryLevel in) {
	}

	@Override
	public void updateExposureTime(ExposureTime in, List<ExposureTime> values) {
	}

	@Override
	public void updateFNumber(FNumber in, List<FNumber> values) {
	}

	@Override
	public void updateFocalLength(FocalLength in) {
	}

	@Override
	public void updateIso(Iso in, List<Iso> values) {
	}
	
	@Override
	public void updateWhiteBalance(WhiteBalanceMode in, List<WhiteBalanceMode> values) {
	}
}
