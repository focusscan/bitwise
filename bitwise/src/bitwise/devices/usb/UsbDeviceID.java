package bitwise.devices.usb;

import bitwise.engine.ID;

public final class UsbDeviceID extends ID {
	private static final String idKind = "UsbDeviceID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	protected UsbDeviceID() {
		super(nextValue());
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
