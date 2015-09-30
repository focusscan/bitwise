package bitwise.gui.eventbus;

import bitwise.engine.eventbus.Event;

public class LoggedEvent {
	private final String eventNumber;
	private final String eventTime;
	private final String eventName;
	private final String eventDescription;
	
	public LoggedEvent(Event in) {
		eventNumber = Integer.toString(in.getID().getValue());
		eventTime = in.getTimeString();
		eventName = in.getName();
		eventDescription = in.getDescription();
	}
	
	public String getEventNumber() {
		return eventNumber;
	}
	
	public String getEventTime() {
		return eventTime;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public String getEventDescription() {
		return eventDescription;
	}
}
