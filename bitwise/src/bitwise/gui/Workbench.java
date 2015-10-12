package bitwise.gui;

import java.io.IOException;

import bitwise.devices.usbservice.UsbDevice;
import bitwise.engine.service.Service;
import bitwise.engine.supervisor.Supervisor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Workbench extends BorderPane {
	public static void showNewWindow(Stage primaryStage) {
		try {
			Workbench view = new Workbench();
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Bitwise");
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
				}
			});
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML private ListView<Service<?, ?>> taskList;
	@FXML private ListView<UsbDevice> deviceList;
	
	public Workbench() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Workbench.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		taskList.setItems(Supervisor.getInstance().getServicesList());
		deviceList.setItems(Supervisor.getInstance().getUsbServiceHandle().getDeviceList());
	}
}
