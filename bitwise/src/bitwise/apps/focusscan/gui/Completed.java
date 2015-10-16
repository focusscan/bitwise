package bitwise.apps.focusscan.gui;

import java.io.IOException;

import bitwise.apps.focusscan.FocusScan;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Completed extends BorderPane {
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
	
	private final FocusScan app;
	
	private Completed(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Completed.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		app.fxdo_Hello(this);
	}
}
