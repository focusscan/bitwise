package bitwise.apps.focusscan3d.gui.deviceselect;

import java.io.IOException;

import bitwise.apps.focusscan3d.FocusScan3D;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.usb.ReadyDevice;
import bitwise.engine.supervisor.Supervisor;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class DeviceSelectView extends BorderPane {
	public static void showNewWindow(Stage primaryStage, FocusScan3D app) {
		try {
			DeviceSelectView view = new DeviceSelectView(app);
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Focus Scan 3D - Device Select");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					app.fx_deviceSelectViewClosed();
				}
			});
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML private TableView<ReadyDevice<?>> tableView;
	@FXML private TableColumn<ReadyDevice<?>, String> colVendorID;
	@FXML private TableColumn<ReadyDevice<?>, String> colProductID;
	@FXML private TableColumn<ReadyDevice<?>, String> colManufacturer;
	@FXML private TableColumn<ReadyDevice<?>, String> colProduct;
	@FXML private TableColumn<ReadyDevice<?>, String> colSerial;
	@FXML private TableColumn<ReadyDevice<?>, String> colDriver;
	@FXML private TableColumn<ReadyDevice<?>, Boolean> colInUse;
	private final ObservableList<ReadyDevice<?>> validDevices;
	
	@FXML private Button btnUseDevice;
	
	private final FocusScan3D app;
	
	private String unknownIfNull(String in) {
		if (null == in)
			return "Unknown";
		return in;
	}
	
	public DeviceSelectView(FocusScan3D in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeviceSelectView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		colVendorID.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = String.format("%04x", p.getValue().getDevice().getVendorID());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colProductID.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = String.format("%04x", p.getValue().getDevice().getProductID());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colManufacturer.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = unknownIfNull(p.getValue().getDevice().getManufacturer());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colProduct.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = unknownIfNull(p.getValue().getDevice().getProduct());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colSerial.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = unknownIfNull(p.getValue().getDevice().getSerialNumber());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colDriver.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<ReadyDevice<?>, String> p) {
		    	 String r = p.getValue().getDriverFactory().getName();
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colInUse.setCellValueFactory(new Callback<CellDataFeatures<ReadyDevice<?>, Boolean>, ObservableValue<Boolean>>() {
		     public ObservableValue<Boolean> call(CellDataFeatures<ReadyDevice<?>, Boolean> p) {
		    	 return p.getValue().getDevice().getDeviceInUse();
		     }
		  });
		
		validDevices = Supervisor.getUSB().getReadyByKind(FullCamera.class);
		tableView.setItems(validDevices);
		
		btnUseDevice.setDisable(true);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ReadyDevice<?>>() {
			@Override
			public void changed(ObservableValue<? extends ReadyDevice<?>> obs, ReadyDevice<?> oldR, ReadyDevice<?> newR) {
				btnUseDevice.setDisable(null == newR || newR.getDevice().getDeviceInUse().get());
			}
		});
	}
	
	@FXML protected void handleUseDevice(ActionEvent event) {
		ReadyDevice<?> rdy = tableView.getSelectionModel().getSelectedItem();
		app.fx_setDriver(rdy);
    }
}
