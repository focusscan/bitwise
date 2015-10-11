package bitwise.usbservice;

import bitwise.engine.ID;

public final class UsbDeviceID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected UsbDeviceID() {
		super(getNextID());
	}
}
