package bitwise.apps;

import bitwise.engine.ID;

public final class AppFactoryID extends ID {
	private static final String idKind = "AppFactoryID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected AppFactoryID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
