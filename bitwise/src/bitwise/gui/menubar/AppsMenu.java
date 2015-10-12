package bitwise.gui.menubar;

import java.io.IOException;

import bitwise.Main;
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
		
		for (AppFactory<?, ?, ?> factory : Supervisor.getInstance().getAppServiceHandle().getAppFactoryList())
			addAppFactory(factory);
	}
	
	private void addAppFactory(AppFactory<?, ?, ?> factory) {
		MenuItem appItem = new MenuItem(factory.getAppName());
		appItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Thread startAppThread = new Thread(new Runnable() {
					@Override
					public void run() {
						Supervisor.getInstance().startApp(Main.getCert(), factory);
					}
				});
				startAppThread.setName(String.format("Start app thread: %s", factory));
				startAppThread.start();
			}
		});
		getItems().add(appItem);
	}
}
