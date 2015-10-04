package bitwise.devices.usb;

import bitwise.engine.ID;

public final class UsbContextID extends ID {
	private static final String idKind = "UsbContextID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected UsbContextID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
