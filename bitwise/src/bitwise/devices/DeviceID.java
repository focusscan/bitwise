package bitwise.devices;

import bitwise.engine.ID;

public final class DeviceID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected DeviceID() {
		super(getNextID());
	}
}
