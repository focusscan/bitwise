package bitwise.apps.focusscan3d.gui.camera;

import java.io.IOException;

import bitwise.apps.focusscan3d.FocusScan3D;
import bitwise.devices.kinds.fullcamera.ExposureIndex;
import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.ExposureTime;
import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FocusMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CameraView extends BorderPane {
	public static CameraView showNewWindow(Stage primaryStage, FocusScan3D app) {
		try {
			CameraView view = new CameraView(app);
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Focus Scan 3D - Camera");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					app.fx_cameraViewClosed();
				}
			});
	        primaryStage.show();
	        return view;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML private Label lblFocalLength;
	@FXML private ComboBox<ExposureMode> cbExposureMode;
	@FXML private ComboBox<FlashMode> cbFlashMode;
	@FXML private ComboBox<FocusMode> cbFocusMode;
	@FXML private ComboBox<FNumber> cbAperture;
	@FXML private ComboBox<ExposureTime> cbExposure;
	@FXML private ComboBox<ExposureIndex> cbIso;
	
	private final FocusScan3D app;
	
	public CameraView(FocusScan3D in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CameraView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		cbExposureMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ExposureMode>() {
			@Override
			public void changed(ObservableValue<? extends ExposureMode> obs, ExposureMode oldV, ExposureMode newV) {
				if (newV.isSetByUser())
					app.fx_setExposureMode(newV);
			}
		});
		cbFlashMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FlashMode>() {
			@Override
			public void changed(ObservableValue<? extends FlashMode> obs, FlashMode oldV, FlashMode newV) {
				if (newV.isSetByUser())
					app.fx_setFlashMode(newV);
			}
		});
		cbFocusMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FocusMode>() {
			@Override
			public void changed(ObservableValue<? extends FocusMode> obs, FocusMode oldV, FocusMode newV) {
				if (newV.isSetByUser())
					app.fx_setFocusMode(newV);
			}
		});
		cbAperture.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FNumber>() {
			@Override
			public void changed(ObservableValue<? extends FNumber> obs, FNumber oldV, FNumber newV) {
				if (newV.isSetByUser())
					app.fx_setAperture(newV);
			}
		});
		cbExposure.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ExposureTime>() {
			@Override
			public void changed(ObservableValue<? extends ExposureTime> obs, ExposureTime oldV, ExposureTime newV) {
				if (newV.isSetByUser())
					app.fx_setExposureTime(newV);
			}
		});
		cbIso.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ExposureIndex>() {
			@Override
			public void changed(ObservableValue<? extends ExposureIndex> obs, ExposureIndex oldV, ExposureIndex newV) {
				if (newV.isSetByUser())
					app.fx_setExposureIndex(newV);
			}
		});
	}
	
	public StringProperty getFocalLengthProperty() {
		return lblFocalLength.textProperty();
	}
	
	public ObservableList<ExposureMode> getExposureModeValues() {
		return cbExposureMode.itemsProperty().get();
	}
	
	public ObjectProperty<ExposureMode> getExposureModeValue() {
		return cbExposureMode.valueProperty();
	}
	
	public ObservableList<FlashMode> getFlashModeValues() {
		return cbFlashMode.itemsProperty().get();
	}
	
	public ObjectProperty<FlashMode> getFlashModeValue() {
		return cbFlashMode.valueProperty();
	}
	
	public ObservableList<FocusMode> getFocusModeValues() {
		return cbFocusMode.itemsProperty().get();
	}
	
	public ObjectProperty<FocusMode> getFocusModeValue() {
		return cbFocusMode.valueProperty();
	}
	
	public ObservableList<FNumber> getApertureValues() {
		return cbAperture.itemsProperty().get();
	}
	
	public ObjectProperty<FNumber> getApertureValue() {
		return cbAperture.valueProperty();
	}
	
	public ObservableList<ExposureTime> getExposureTimeValues() {
		return cbExposure.itemsProperty().get();
	}
	
	public ObjectProperty<ExposureTime> getExposureTimeValue() {
		return cbExposure.valueProperty();
	}
	
	public ObservableList<ExposureIndex> getExposureIndexValues() {
		return cbIso.itemsProperty().get();
	}
	
	public ObjectProperty<ExposureIndex> getExposureIndexValue() {
		return cbIso.valueProperty();
	}
}
