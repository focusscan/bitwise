package bitwise.apps;

import bitwise.engine.ID;

public final class AppID extends ID {
	private static final String idKind = "AppID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected AppID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
