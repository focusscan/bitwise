package bitwise.gui.imageview;

import java.io.IOException;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	private ReadOnlyDoubleProperty fitWidth = this.widthProperty();
	private ReadOnlyDoubleProperty fitHeight = this.heightProperty();
	
	public void setFitDimensions(ReadOnlyDoubleProperty in_fitWidth, ReadOnlyDoubleProperty in_fitHeight) {
		fitWidth = in_fitWidth;
		fitWidth.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (null != image)
					setImage(image);
			}
		});
		fitHeight = in_fitHeight;
		fitHeight.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (null != image)
					setImage(image);
			}
		});
	}
	
	private Image image = null;
	
	public void setImage(Image in) {
		image = in;
		double tw = fitWidth.doubleValue();
		double th = fitHeight.doubleValue();
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
			if (scaleBy > 1)
				scaleBy = 1;
			imageView.fitWidthProperty().set(iw * scaleBy);
			imageView.fitHeightProperty().set(ih * scaleBy);
		}
		imageView.setImage(in);
	}
}
