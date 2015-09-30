package bitwise.gui.workbench.menubar.menus;

import java.io.IOException;

import bitwise.apps.AppFactory;
import bitwise.engine.supervisor.Supervisor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class AppsMenu extends Menu {
	public AppsMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppsMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		for (AppFactory<?> appFactory : Supervisor.getApps().getAppFactories())
			addAppToMenu(appFactory);
	}
	
	private void addAppToMenu(AppFactory<?> factory) {
		MenuItem appMenuItem = new MenuItem(factory.getName());
		appMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Supervisor.getApps().launchApp(factory.getID());
			}
		});
		getItems().add(appMenuItem);
	}
}
