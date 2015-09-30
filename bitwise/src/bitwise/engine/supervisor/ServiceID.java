package bitwise.engine.supervisor;

import bitwise.engine.ID;

public final class ServiceID extends ID {
	private static final String idKind = "ServiceID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected ServiceID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
