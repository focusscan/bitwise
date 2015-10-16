package bitwise.gui.imageview;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AspectImageView extends Pane {
	@FXML private ImageView imageView;
	
	public AspectImageView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AspectImageView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
	
	public void setImage(Image in) {
		double tw = this.getWidth();
		double th = this.getHeight();
		double iw = in.getWidth();
		double ih = in.getHeight();
		
		if (0 == tw || 0 == th || 0 == iw || 0 == ih) {
			imageView.fitWidthProperty().set(tw);
			imageView.fitHeightProperty().set(th);
		}
		else {
			double scaleBy = 1;
			if (tw / th < iw / ih)
				scaleBy = tw / iw;
			else
				scaleBy = th / ih;
			imageView.fitWidthProperty().set(iw * scaleBy);
			imageView.fitHeightProperty().set(ih * scaleBy);
		}
		imageView.setImage(in);
	}
}
