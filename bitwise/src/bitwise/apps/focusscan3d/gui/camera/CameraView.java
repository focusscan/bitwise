package bitwise.apps.focusscan3d.gui.camera;

import java.io.IOException;

import bitwise.apps.focusscan3d.FocusScan3D;
import bitwise.devices.kinds.fullcamera.FNumber;
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
	@FXML private ComboBox<FNumber> cbAperture;
	
	private final FocusScan3D app;
	
	public CameraView(FocusScan3D in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CameraView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		cbAperture.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FNumber>() {
			@Override
			public void changed(ObservableValue<? extends FNumber> obs, FNumber oldV, FNumber newV) {
				app.fx_setAperture(newV);
			}
		});
	}
	
	public StringProperty getFocalLengthProperty() {
		return lblFocalLength.textProperty();
	}
	
	public ObservableList<FNumber> getApertureValues() {
		return cbAperture.itemsProperty().get();
	}
	
	public ObjectProperty<FNumber> getApertureValue() {
		return cbAperture.valueProperty();
	}
}
