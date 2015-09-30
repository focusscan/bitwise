package bitwise.gui.workbench.menubar.menus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public class EditMenu extends Menu {
	public EditMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
