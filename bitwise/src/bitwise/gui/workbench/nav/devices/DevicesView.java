package bitwise.gui.workbench.nav.devices;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

public class DevicesView extends ListView<Object> {
	public DevicesView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DevicesView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
