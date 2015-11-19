package bitwise.apps.focusscan.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import bitwise.apps.focusscan.FocusScan;
import bitwise.devices.camera.*;
import bitwise.engine.supervisor.Supervisor;
import bitwise.filesystem.Directory;
import bitwise.gui.imageview.AspectImageView;
import bitwise.log.Log;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScanSetup extends BorderPane implements CameraPropListener {
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
	@FXML private TextField scanName;
	@FXML private TextField focusSteps;
	@FXML private TextField focusStepsPerImage;
	@FXML private Button btnScan;
	@FXML private Label imageCount;
	@FXML private AspectImageView imageView;
	private final FocusScan app;
	
	private ScanSetup(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScanSetup.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		cbIso.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Iso>() {
			@Override
			public void changed(ObservableValue<? extends Iso> obs, Iso oldV, Iso newV) {
				if (null != newV && null != oldV)
					app.fxdo_setIso(newV);
			}
		});
		cbAperture.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FNumber>() {
			@Override
			public void changed(ObservableValue<? extends FNumber> obs, FNumber oldV, FNumber newV) {
				if (null != newV && null != oldV)
					app.fxdo_setFNumber(newV);
			}
		});
		cbExposure.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ExposureTime>() {
			@Override
			public void changed(ObservableValue<? extends ExposureTime> obs, ExposureTime oldV, ExposureTime newV) {
				if (null != newV && null != oldV)
					app.fxdo_setExposureTime(newV);
			}
		});
		focusSteps.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateImageCount();
			}
		});
		focusStepsPerImage.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateImageCount();
			}
		});
	}
	
	public void setImage(Image image) {
		imageView.setImage(image);
	}
	
	@FXML protected void focusNear(ActionEvent event) {
		try {
			int steps = Integer.parseInt(focusSteps.getText());
			app.fxdo_focusNear(steps);
		} catch (Exception e) {
			Log.log(app, "cannot parse %s", focusSteps.getText());
		}
	}
	
	@FXML protected void focusFar(ActionEvent event) {
		try {
			int steps = Integer.parseInt(focusSteps.getText());
			app.fxdo_focusFar(steps);
		} catch (Exception e) {
			Log.log(app, "cannot parse %s", focusSteps.getText());
		}
	}
	
	@FXML protected void takeTestImage(ActionEvent event) {
		app.fxdo_takeTestImage();
	}
	
	private void updateImageCount() {
		try {
			int steps = Integer.parseInt(focusSteps.getText());
			int stepsPerImage = Integer.parseInt(focusStepsPerImage.getText());
			int images = 1 + (steps / stepsPerImage);
			imageCount.setText(String.format("%d", images));
			btnScan.setDisable(false);
		} catch (Exception e) {
			imageCount.setText("N/A");
			btnScan.setDisable(true);
		}
	}
	
	@FXML protected void scanNearToFar(ActionEvent event) {
		try {
			Directory workpath = Supervisor.getInstance().getFileSystemServiceHandle().getWorkpath();
			Path scanPath = workpath.getPath().resolve(scanName.getText());
			if (Files.exists(scanPath))
				return;
			int steps = Integer.parseInt(focusSteps.getText());
			int stepsPerImage = Integer.parseInt(focusStepsPerImage.getText());
			app.fxdo_scanNearToFar(scanPath, steps, stepsPerImage);
		} catch (Exception e) {
			Log.log(app, "cannot parse %s %s", focusSteps.getText(), focusStepsPerImage.getText());
		}
	}

	@Override
	public void updateBatteryLevel(BatteryLevel in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExposureTime(ExposureTime in, List<ExposureTime> values) {
		cbExposure.itemsProperty().get().clear();
		cbExposure.itemsProperty().get().addAll(values);
		cbExposure.setValue(in);
	}

	@Override
	public void updateFNumber(FNumber in, List<FNumber> values) {
		cbAperture.itemsProperty().get().clear();
		cbAperture.itemsProperty().get().addAll(values);
		cbAperture.setValue(in);
	}

	@Override
	public void updateFocalLength(FocalLength in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateIso(Iso in, List<Iso> values) {
		cbIso.itemsProperty().get().clear();
		cbIso.itemsProperty().get().addAll(values);
		cbIso.setValue(in);
	}
}
