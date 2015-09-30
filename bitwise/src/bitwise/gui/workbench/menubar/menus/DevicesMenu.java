package bitwise.gui.workbench.menubar.menus;

import java.io.IOException;

import bitwise.gui.usbexplorer.UsbExplorerView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public class DevicesMenu extends Menu {
	public DevicesMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DevicesMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
	
	@FXML private void showUsbDeviceExplorer(ActionEvent event) {
		UsbExplorerView.showNewWindow(null);
	}
}
