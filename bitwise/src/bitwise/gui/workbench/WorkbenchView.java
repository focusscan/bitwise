package bitwise.gui.workbench;

import java.io.IOException;

import bitwise.engine.supervisor.Supervisor;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WorkbenchView extends BorderPane {
	private static void showNewWindow(Stage primaryStage) {
		try {
			WorkbenchView view = new WorkbenchView();
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Bitwise Workbench");
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					stopBitwise();
				}
			});
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startBitwise(Stage primaryStage) {
		Task<Void> startBitwiseTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Supervisor.startBitwise();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showNewWindow(primaryStage);
					}
				});
				return null;
			}
		};
		Thread th = new Thread(startBitwiseTask);
		th.setName("Start Bitwise");
		th.setDaemon(true);
		th.start();
	}
	
	private static void stopBitwise() {
		Task<Void> shutdownBitwiseTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Supervisor.shutdownBitwise();
				return null;
			}
		};
		Thread th = new Thread(shutdownBitwiseTask);
		th.setName("Shutdown Bitwise");
		th.setDaemon(true);
		th.start();
	}
	
	public WorkbenchView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WorkbenchView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
