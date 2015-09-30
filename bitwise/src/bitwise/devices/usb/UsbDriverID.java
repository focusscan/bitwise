package bitwise.devices.usb;

import bitwise.engine.ID;

public final class UsbDriverID extends ID {
	private static final String idKind = "UsbDriverID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected UsbDriverID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
