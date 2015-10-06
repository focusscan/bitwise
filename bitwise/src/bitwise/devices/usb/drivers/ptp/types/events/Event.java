package bitwise.devices.usb.drivers.ptp.types.events;

public abstract class Event {
	private final String name;
	private final EventCode eventCode;
	
	public Event(String in_name, EventCode in_eventCode) {
		name = in_name;
		eventCode = in_eventCode;
		assert(null != name);
		assert(null != eventCode);
	}
	
	public String getName() {
		return name;
	}
	
	public EventCode getEventCode() {
		return eventCode;
	}
}
