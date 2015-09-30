package bitwise.engine.supervisor.events;

import bitwise.engine.eventbus.Event;


public final class BitwiseTerminatedEvent extends Event {
	
	public BitwiseTerminatedEvent() {
		super("Bitwise Terminated");
	}
	
	@Override
	public String getDescription() {
		return "Bitwise terminated.";
	}
}
