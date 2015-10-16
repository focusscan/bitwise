package bitwise.gui;

import java.io.IOException;

import bitwise.devices.usbservice.UsbDevice;
import bitwise.engine.service.BaseService;
import bitwise.engine.supervisor.Supervisor;
import bitwise.gui.filesystem.FileSystem;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Workbench extends BorderPane {
	public static void showNewWindow(Stage primaryStage) {
		try {
			Workbench view = new Workbench();
			Scene scene = new Scene(view);
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
	
	@FXML private Accordion leftAccordion;
	@FXML private TitledPane tpProject;
	@FXML private TitledPane tpTasks;
	@FXML private TitledPane tpDevices;
	@FXML private FileSystem fileSystem;
	@FXML private ListView<BaseService<?>> taskList;
	@FXML private ListView<UsbDevice> deviceList;
	@FXML private TabPane openTabs;
	
	public Workbench() throws IOException {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Workbench.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		fileSystem.setWorkbench(this);
		
		leftAccordion.setExpandedPane(tpProject);
		
		taskList.setItems(Supervisor.getInstance().getServicesList());
		deviceList.setItems(Supervisor.getInstance().getUsbServiceHandle().getDeviceList());
	}
	
	public void addTab(Tab in) {
		openTabs.getTabs().add(in);
		openTabs.getSelectionModel().select(in);
	}
}
