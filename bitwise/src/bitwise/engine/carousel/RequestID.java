package bitwise.engine.carousel;

import bitwise.engine.ID;

public final class RequestID extends ID {
	private static final String idKind = "RequestID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected RequestID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
