package bitwise.apps.events;

import bitwise.apps.AppService;
import bitwise.engine.eventbus.Event;


public final class AppHelloEvent extends Event {
	private final AppService apps;
	
	public AppHelloEvent(AppService in_apps) {
		super("Apps Hello");
		apps = in_apps;
		assert(null != apps);
	}

	@Override
	public String getDescription() {
		return String.format("Apps [%s] started and ready to process new requests.", apps);
	}
}
