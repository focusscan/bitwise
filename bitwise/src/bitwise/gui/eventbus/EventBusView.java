package bitwise.gui.eventbus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bitwise.engine.eventbus.EventNode;
import bitwise.engine.supervisor.Supervisor;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class EventBusView extends BorderPane {
	public static void showNewWindow(Stage primaryStage) {
		try {
			EventBusView view = new EventBusView();
			Scene scene = new Scene(view);
			if (null == primaryStage)
				primaryStage = new Stage();
			primaryStage.setTitle("Bitwise - Event Bus Monitor");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					view.shutdownMonitor();
				}
			});
	        primaryStage.show();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML private TableView<LoggedEvent> tableView;
	@FXML private TableColumn<LoggedEvent, String> colEventNum;
	@FXML private TableColumn<LoggedEvent, String> colEventTime;
	@FXML private TableColumn<LoggedEvent, String> colEventKind;
	@FXML private TableColumn<LoggedEvent, String> colEventDesc;
	final ObservableList<LoggedEvent> events = FXCollections.observableArrayList();
	
	final Task<EventNode> monitorTask;
	
	public EventBusView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventBusView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		colEventNum.setCellValueFactory(new Callback<CellDataFeatures<LoggedEvent, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<LoggedEvent, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getEventNumber());
		     }
		  });
		colEventTime.setCellValueFactory(new Callback<CellDataFeatures<LoggedEvent, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<LoggedEvent, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getEventTime());
		     }
		  });
		colEventKind.setCellValueFactory(new Callback<CellDataFeatures<LoggedEvent, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<LoggedEvent, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getEventName());
		     }
		  });
		colEventDesc.setCellValueFactory(new Callback<CellDataFeatures<LoggedEvent, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<LoggedEvent, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getEventDescription());
		     }
		  });
		tableView.setItems(events);
		
		monitorTask = new Task<EventNode>() {
			@Override
			protected EventNode call() throws Exception {
				EventNode lastAdded = null;
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				do {
					if (lastAdded != eventNode) {
						lastAdded = eventNode;
						events.add(new LoggedEvent(lastAdded.getEvent()));
					}
					EventNode next = eventNode.waitLimitedOnNext(2, TimeUnit.MILLISECONDS);
					if (null != next)
						eventNode = next;
				} while (!isCancelled());
				return eventNode;
			}
		};
		
		Thread th = new Thread(monitorTask);
		th.setName("EventBusView monitor");
		th.setDaemon(true);
		th.start();
	}
	
	public void shutdownMonitor() {
		monitorTask.cancel();
	}
}
