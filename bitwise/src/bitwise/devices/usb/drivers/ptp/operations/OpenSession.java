package bitwise.devices.usb.drivers.ptp.operations;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class OpenSession extends Operation {
	public static final UInt16 operationCode = new UInt16((short) 0x1002);
	
	private final SessionID sessionID;
	
	public OpenSession() {
		super("OpenSession", TransactionID.zero, operationCode, 1, null);
		sessionID = new SessionID();
		getArguments().add(sessionID);
	}
}
