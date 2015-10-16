package bitwise.apps.focusscan.gui;

import java.io.IOException;

import bitwise.apps.focusscan.FocusScan;
import bitwise.gui.imageview.AspectImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Scan extends BorderPane {
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
	
	@FXML private AspectImageView imageView;
	private final FocusScan app;
	
	private Scan(FocusScan in_app) throws IOException {
		app = in_app;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scan.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		app.fxdo_Hello(this);
	}
	
	public void setImage(Image image) {
		imageView.setImage(image);
	}
}
