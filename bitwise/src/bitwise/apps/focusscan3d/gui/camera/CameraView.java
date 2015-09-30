package bitwise.apps.focusscan3d.gui.camera;

import java.io.IOException;

import bitwise.apps.focusscan3d.FocusScan3D;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CameraView extends BorderPane {
	public static void showNewWindow(Stage primaryStage, FocusScan3D app) {
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
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final FocusScan3D app;
	
	public CameraView(FocusScan3D in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CameraView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
