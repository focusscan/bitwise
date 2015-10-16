package bitwise.gui;

import java.io.File;
import java.io.IOException;

import bitwise.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PathChooser extends BorderPane {
	public static void showNewWindow(Stage primaryStage) {
		try {
			PathChooser view = new PathChooser(primaryStage);
			Scene scene = new Scene(view);
			primaryStage.setTitle("Bitwise - Choose work path");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
				}
			});
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML private TextField path;
	private final Stage stage;
	
	public PathChooser(Stage in_stage) throws IOException {
		super();
		stage = in_stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PathChooser.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
	
	@FXML private void browse(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Bitwise workspace");
		File file = directoryChooser.showDialog(stage);
		if (file != null) {
			path.setText(file.getAbsolutePath());
		}
	}
	
	@FXML private void open(ActionEvent event) {
		Thread initialize = new Thread(new Runnable() {
			@Override
			public void run() {
				final boolean initialized = Main.startBitwise(path.getText());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (initialized)
							Workbench.showNewWindow(stage);
					}
				});
			}
		});
		initialize.setName("Bitwise initialize thread");
		initialize.start();
	}
}
