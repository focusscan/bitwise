package bitwise.gui.workbench.menubar.menus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public class FileMenu extends Menu {
	public FileMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
