package bitwise.gui.filesystem;

import java.io.IOException;

import bitwise.gui.imageview.AspectImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;

public class ImageTab extends Tab {
	@FXML private AspectImageView imageView;
	
	public ImageTab() throws IOException {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageTab.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
	
	public void setImage(Image in) {
		imageView.setImage(in);
	}
}
