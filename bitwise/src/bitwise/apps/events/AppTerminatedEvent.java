package bitwise.apps.events;

import bitwise.apps.App;
import bitwise.engine.eventbus.Event;


public final class AppTerminatedEvent extends Event {
	private final App app;
	
	public AppTerminatedEvent(App in_app) {
		super("App Terminated");
		app = in_app;
	}
	
	public App getApp() {
		return app;
	}
	
	@Override
	public String getDescription() {
		return String.format("App (%s) terminated.", app);
	}
}
