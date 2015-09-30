package bitwise.engine.supervisor.events;

import bitwise.engine.eventbus.Event;


public final class BitwiseStartedEvent extends Event {
	
	public BitwiseStartedEvent() {
		super("Bitwise Started");
	}
	
	@Override
	public String getDescription() {
		return "Bitwise started.";
	}
}
