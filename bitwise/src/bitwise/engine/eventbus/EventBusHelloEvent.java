package bitwise.engine.eventbus;


public final class EventBusHelloEvent extends Event {
	protected EventBusHelloEvent() {
		super("EventBus Hello");
	}

	@Override
	public String getDescription() {
		return "EventBus has started.";
	}
}
