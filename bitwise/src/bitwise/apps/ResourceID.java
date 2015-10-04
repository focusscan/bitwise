package bitwise.apps;

import bitwise.engine.ID;

public abstract class ResourceID extends ID {
	private static final String idKind = "RequestID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected ResourceID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
