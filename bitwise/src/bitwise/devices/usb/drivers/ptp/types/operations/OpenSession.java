package bitwise.devices.usb.drivers.ptp.types.operations;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;

public class OpenSession extends Operation {
	private final SessionID sessionID;
	
	public OpenSession() {
		super("OpenSession", TransactionID.zero, OperationCode.openSession, 1, null);
		sessionID = new SessionID();
		getArguments().add(sessionID);
	}
}
