package bitwise.gui.workbench.nav.jobs;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

public class JobsView extends ListView<Object> {
	public JobsView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JobsView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
