package bitwise.engine.service;

import bitwise.engine.ID;

public final class RequestContextID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected RequestContextID() {
		super(getNextID());
	}
}
