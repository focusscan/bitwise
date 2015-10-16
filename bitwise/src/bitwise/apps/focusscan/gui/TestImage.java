package bitwise.apps.focusscan.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import bitwise.gui.imageview.AspectImageView;

public class TestImage extends BorderPane {
	public static TestImage showTestImage(Stage primaryStage) {
		try {
			TestImage view = new TestImage();
			Scene scene = new Scene(view);
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Focus Scan - Test Image");
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
	
	private TestImage() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TestImage.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		imageView.setFitDimensions(this.widthProperty(), this.heightProperty());
	}
	
	public void setImage(Image in) {
		imageView.setImage(in);
	}
}
