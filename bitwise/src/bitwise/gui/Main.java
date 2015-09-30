package bitwise.gui;

import bitwise.gui.eventbus.EventBusView;
import bitwise.gui.workbench.WorkbenchView;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		EventBusView.showNewWindow(primaryStage);
		WorkbenchView.startBitwise(null);
	}
	
	public static void main(String[] args) {
		System.out.println("Bitwise GUI running.");
		launch(args);
		System.out.println("Bitwise GUI exited.");
	}
}
