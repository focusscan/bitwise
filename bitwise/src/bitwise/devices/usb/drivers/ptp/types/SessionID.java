package bitwise.devices.usb.drivers.ptp.types;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class SessionID extends UInt32 {
	public static final SessionID zero = new SessionID(0);
	
	public SessionID(int in_value) {
		super(in_value);
	}
}
