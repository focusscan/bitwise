package bitwise.apps.focusscan.gui;

import java.io.IOException;
import java.util.List;

import bitwise.apps.focusscan.FocusScan;
import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.Iso;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Completed extends BorderPane implements CameraPropListener {
	public static Completed showCompleted(FocusScan app, Stage primaryStage) {
		try {
			Completed view = new Completed(app);
			Scene scene = new Scene(view);
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Focus Scan - Completed");
	        primaryStage.show();
	        return view;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Completed(FocusScan in_app) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Completed.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
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
}
