package bitwise.engine.carousel;

import bitwise.engine.ID;

public final class ContextID extends ID {
	private static final String idKind = "ContextID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected ContextID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
