package bitwise.apps.events;

import bitwise.apps.AppService;
import bitwise.engine.eventbus.Event;


public final class AppFastGoodbyeEvent extends Event {
	private final AppService apps;
	
	public AppFastGoodbyeEvent(AppService in_apps) {
		super("Apps Fast Goodbye");
		apps = in_apps;
		assert(null != apps);
	}
	
	@Override
	public String getDescription() {
		return String.format("Apps [%s] shutting down. No new requests will be served; existing requests will be asked to abort.", apps);
	}
}
