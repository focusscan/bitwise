package bitwise.apps.focusscan.gui;

import java.io.IOException;
import java.util.List;

import bitwise.apps.focusscan.FocusScan;
import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.WhiteBalanceMode;
import bitwise.gui.imageview.AspectImageView;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Scan extends BorderPane implements CameraPropListener {
	public static Scan showScan(FocusScan app, Stage primaryStage) {
		try {
			Scan view = new Scan(app);
			Scene scene = new Scene(view);
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Focus Scan - Scanning");
	        primaryStage.show();
	        return view;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML private Button btnPause;
	@FXML private ProgressBar batteryLevel;
	@FXML private HBox border;
	@FXML private AspectImageView imageViewLV;
	@FXML private AspectImageView imageViewSI;
	private final FocusScan app;
	
	private Scan(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scan.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		DoubleProperty width = new SimpleDoubleProperty(getWidth());
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				width.set(newValue.doubleValue() / 2);
			}
		});
		imageViewLV.setFitDimensions(width, border.heightProperty());
		imageViewSI.setFitDimensions(width, border.heightProperty());
	}
	
	public void setImageLV(Image image) {
		imageViewLV.setImage(image);
	}
	
	public void setImageSI(Image image) {
		imageViewSI.setImage(image);
	}
	
	private boolean isPaused = false;
	
	@FXML protected void pauseScan(ActionEvent event) {
		if (!isPaused) {
			app.fxdo_pauseScan();
			isPaused = true;
			btnPause.setText("Unpause");
		}
		else {
			app.fxdo_unpauseScan();
			isPaused = false;
			btnPause.setText("Pause");
		}
	}
	
	@FXML protected void cancelScan(ActionEvent event) {
		app.fxdo_scanCancelled();
	}

	@Override
	public void updateBatteryLevel(BatteryLevel in) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				batteryLevel.setProgress(in.getValue() / 100.0);
			}
		});
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
