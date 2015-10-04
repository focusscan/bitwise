package bitwise.devices.usb.drivers;

import bitwise.engine.ID;

public final class UsbDriverFactoryID extends ID {
	private static final String idKind = "UsbDriverFactoryID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected UsbDriverFactoryID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
