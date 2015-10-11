package bitwise.engine.service.requests;

import bitwise.engine.ID;

public final class RequestID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected RequestID() {
		super(getNextID());
	}
}
