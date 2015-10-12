package bitwise.gui.menubar;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

public class WorkbenchMenuBar extends MenuBar {
	public WorkbenchMenuBar() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WorkbenchMenuBar.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
