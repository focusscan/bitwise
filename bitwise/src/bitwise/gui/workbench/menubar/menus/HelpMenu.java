package bitwise.gui.workbench.menubar.menus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public class HelpMenu extends Menu {
	public HelpMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelpMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
