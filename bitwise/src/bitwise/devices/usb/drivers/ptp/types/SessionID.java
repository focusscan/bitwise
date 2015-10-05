package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class SessionID extends UInt32 {
	public static final SessionID zero = new SessionID(0);
	
	private static int nextSessionID = 1;
	private synchronized static int getNextSessionID() {
		return nextSessionID++;
	}
	
	public SessionID(ByteBuffer in) {
		super(in);
	}
	
	public SessionID(int in_value) {
		super(in_value);
	}
	
	public SessionID() {
		super(getNextSessionID());
	}
}
