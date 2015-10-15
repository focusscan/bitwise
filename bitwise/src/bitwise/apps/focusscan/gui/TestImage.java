package bitwise.apps.focusscan.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestImage extends BorderPane {
	public static TestImage showTestImage(Image image, Stage primaryStage) {
		try {
			TestImage view = new TestImage(image);
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
	
	@FXML private Pane centerPane;
	@FXML private ImageView imageView;
	
	private TestImage(Image image) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TestImage.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		imageView.setImage(image);
		
		imageView.fitHeightProperty().bind(centerPane.heightProperty());
		imageView.fitWidthProperty().bind(centerPane.widthProperty());
	}
}
