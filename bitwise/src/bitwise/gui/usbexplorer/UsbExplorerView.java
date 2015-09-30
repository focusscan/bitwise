package bitwise.gui.usbexplorer;

import java.io.IOException;

import bitwise.devices.usb.UsbDevice;
import bitwise.engine.supervisor.Supervisor;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UsbExplorerView extends TableView<UsbDevice> {
	public static void showNewWindow(Stage primaryStage) {
		try {
			UsbExplorerView view = new UsbExplorerView();
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Bitwise - USB Explorer");
			primaryStage.setScene(scene);
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML private TableColumn<UsbDevice, String> colVendorID;
	@FXML private TableColumn<UsbDevice, String> colProductID;
	@FXML private TableColumn<UsbDevice, String> colManufacturer;
	@FXML private TableColumn<UsbDevice, String> colProduct;
	@FXML private TableColumn<UsbDevice, String> colSerial;
	@FXML private TableColumn<UsbDevice, Boolean> colInUse;
	
	private String unknownIfNull(String in) {
		if (null == in)
			return "Unknown";
		return in;
	}
	
	public UsbExplorerView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UsbExplorerView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		colVendorID.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UsbDevice, String> p) {
		    	 String r = String.format("%04x", p.getValue().getVendorID());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colProductID.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UsbDevice, String> p) {
		    	 String r = String.format("%04x", p.getValue().getProductID());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colManufacturer.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UsbDevice, String> p) {
		    	 String r = unknownIfNull(p.getValue().getManufacturer());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colProduct.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UsbDevice, String> p) {
		    	 String r = unknownIfNull(p.getValue().getProduct());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colSerial.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UsbDevice, String> p) {
		    	 String r = unknownIfNull(p.getValue().getSerialNumber());
		         return new ReadOnlyObjectWrapper<String>(r);
		     }
		  });
		colInUse.setCellValueFactory(new Callback<CellDataFeatures<UsbDevice, Boolean>, ObservableValue<Boolean>>() {
		     public ObservableValue<Boolean> call(CellDataFeatures<UsbDevice, Boolean> p) {
		    	 return p.getValue().getDeviceInUse();
		     }
		  });
		setItems(Supervisor.getUSB().getDevices());
	}
}
