package bitwise.engine.service;

import bitwise.engine.ID;

public final class ServiceTaskID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected ServiceTaskID() {
		super(getNextID());
	}
}
