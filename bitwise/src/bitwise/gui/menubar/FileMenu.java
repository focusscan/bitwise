package bitwise.gui.menubar;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {
	@FXML private MenuItem menuExit;
	
	public FileMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
	
	@FXML
	private void handleExit(ActionEvent event) {
		Platform.exit();
	}
}
