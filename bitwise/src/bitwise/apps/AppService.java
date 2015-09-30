package bitwise.apps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import bitwise.apps.events.AppFastGoodbyeEvent;
import bitwise.apps.events.AppGoodbyeEvent;
import bitwise.apps.events.AppHelloEvent;
import bitwise.apps.events.AppTerminatedEvent;
import bitwise.engine.supervisor.Service;
import bitwise.engine.supervisor.Supervisor;

public class AppService extends Service {
	public static String serviceName() {
		return "Apps";
	}
	
	public static int threadPoolSize() {
		return 4;
	}
	
	private final ObservableList<AppFactory<?>> appFactories = FXCollections.observableArrayList();
	private final ObservableList<App> apps = FXCollections.observableArrayList();
	
	public AppService() {
		super(serviceName(), threadPoolSize());
	}
	
	public synchronized void installAppFactory(AppFactory<?> factory) {
		appFactories.add(factory);
	}
	
	public ObservableList<AppFactory<?>> getAppFactories() {
		return appFactories;
	}
	
	@Override
	protected synchronized void onStart() {
		Supervisor.getEventBus().publishEventToBus(new AppHelloEvent(this));
	}
	
	@Override
	protected synchronized void onShutdown() {
		for (App app : apps)
			app.terminate();
		Supervisor.getEventBus().publishEventToBus(new AppGoodbyeEvent(this));
	}
	
	@Override
	protected synchronized void onShutdownNow() {
		for (App app : apps)
			app.terminate();
		Supervisor.getEventBus().publishEventToBus(new AppFastGoodbyeEvent(this));
	}
	
	public synchronized void launchApp(AppFactoryID factoryID) {
		AppFactory<?> factory = null;
		for (AppFactory<?> looking : appFactories) {
			if (looking.getID().equals(factoryID)) {
				factory = looking;
				break;
			}
		}
		assert(null != factory);
		App app = factory.makeApp();
		apps.add(app);
		getExecutor().submit(app);
	}
	
	protected synchronized void notifyAppTerminated(App in) {
		boolean removed = apps.remove(in);
		assert(removed);
		Supervisor.getEventBus().publishEventToBus(new AppTerminatedEvent(in));
	}
}
