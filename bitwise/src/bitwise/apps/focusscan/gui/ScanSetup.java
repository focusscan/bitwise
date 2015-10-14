package bitwise.apps.focusscan.gui;

import java.io.IOException;
import java.util.List;

import bitwise.apps.focusscan.FocusScan;
import bitwise.devices.camera.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScanSetup extends BorderPane {
	public static ScanSetup showScanSetup(FocusScan app, Stage primaryStage) {
		try {
			ScanSetup view = new ScanSetup(app);
			Scene scene = new Scene(view);
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Focus Scan - Scan Setup");
	        primaryStage.show();
	        return view;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML private ComboBox<Iso> cbIso;
	@FXML private ComboBox<FNumber> cbAperture;
	@FXML private ComboBox<ExposureTime> cbExposure;
	private final FocusScan app;
	
	private ScanSetup(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScanSetup.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		app.fxdo_Hello(this);
		
		cbIso.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Iso>() {
			@Override
			public void changed(ObservableValue<? extends Iso> obs, Iso oldV, Iso newV) {
				if (null != newV)
					app.fxdo_setIso(newV);
			}
		});
		cbAperture.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FNumber>() {
			@Override
			public void changed(ObservableValue<? extends FNumber> obs, FNumber oldV, FNumber newV) {
				if (null != newV)
					app.fxdo_setFNumber(newV);
			}
		});
		cbExposure.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ExposureTime>() {
			@Override
			public void changed(ObservableValue<? extends ExposureTime> obs, ExposureTime oldV, ExposureTime newV) {
				if (null != newV)
					app.fxdo_setExposureTime(newV);
			}
		});
	}
	
	public void setBatteryLevel(BatteryLevel value) {
		
	}
	
	public void setExposureProgramMode(ExposureProgramMode value) {
		
	}
	
	public void setExposureTime(ExposureTime value, List<ExposureTime> values) {
		cbExposure.itemsProperty().get().clear();
		cbExposure.itemsProperty().get().addAll(values);
		cbExposure.setValue(value);
	}
	
	public void setFlashMode(FlashMode value) {
		
	}
	
	public void setFNumber(FNumber value, List<FNumber> values) {
		cbAperture.itemsProperty().get().clear();
		cbAperture.itemsProperty().get().addAll(values);
		cbAperture.setValue(value);
	}
	
	public void setFocalLength(FocalLength value) {
		
	}
	
	public void setFocusMode(FocusMode value) {
		
	}
	
	public void setIso(Iso value, List<Iso> values) {
		cbIso.itemsProperty().get().clear();
		cbIso.itemsProperty().get().addAll(values);
		cbIso.setValue(value);
	}
	
	public void setStorageDevices(List<StorageDevice> values) {
		
	}
	
	public void setImageFormats(List<ImageFormat> values) {
		
	}
}
