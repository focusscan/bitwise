package bitwise.engine.eventbus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Event {
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private final EventID id;
	private final Date time;
	private final String name;
	
	public Event(String in_name) {
		id = new EventID();
		name = in_name;
		assert(null != name);
		time = Calendar.getInstance().getTime();
		assert(null != time);
	}
	
	public EventID getID() {
		return id;
	}
	
	public Date getTime() {
		return time;
	}
	
	public String getTimeString() {
		return sdf.format(time);
	}
	
	public String getName() {
		return name;
	}
	
	public abstract String getDescription();
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c eventNumber is unique
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("Event<%08x> [%s] %s: %s", id.getValue(), sdf.format(time), name, getDescription());
	}
}
