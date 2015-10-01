package bitwise.devices.usb;

import bitwise.engine.ID;

public final class UsbRequestID extends ID {
	private static final String idKind = "UsbRequestID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected UsbRequestID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
