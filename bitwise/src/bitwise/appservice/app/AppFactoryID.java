package bitwise.appservice.app;

import bitwise.engine.ID;

public final class AppFactoryID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected AppFactoryID() {
		super(getNextID());
	}
}
