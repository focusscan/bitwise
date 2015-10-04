package bitwise.devices.usb.drivers;

import bitwise.apps.ResourceID;

public final class UsbDriverID extends ResourceID {
	private static final String idKind = "UsbDriverID";
	
	protected UsbDriverID() {
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
