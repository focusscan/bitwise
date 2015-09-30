package bitwise.apps.events;

import bitwise.apps.App;
import bitwise.engine.eventbus.Event;


public final class AppLaunchedEvent extends Event {
	private final App app;
	
	public AppLaunchedEvent(App in_app) {
		super("App Launched");
		app = in_app;
	}
	
	public App getApp() {
		return app;
	}
	
	@Override
	public String getDescription() {
		return String.format("App (%s) launched.", app);
	}
}
