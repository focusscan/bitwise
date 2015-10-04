package bitwise.devices.usb;

import bitwise.apps.RequestID;

public final class UsbRequestID extends RequestID {
	private static final String idKind = "UsbRequestID";
	
	protected UsbRequestID() {
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
