package bitwise.devices.usbservice;

import bitwise.engine.ID;

public final class UsbDriverFactoryID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected UsbDriverFactoryID() {
		super(getNextID());
	}
}
