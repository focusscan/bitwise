package bitwise.engine.eventbus;

import bitwise.engine.ID;

public final class EventID extends ID {
	private static final String idKind = "EventID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected EventID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
