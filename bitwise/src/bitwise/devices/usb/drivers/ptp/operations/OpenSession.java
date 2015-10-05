package bitwise.devices.usb.drivers.ptp.operations;

import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class OpenSession extends BaseOperation {
	public static final UInt16 operationCode = new UInt16((short) 0x1002);
	
	private final SessionID sessionID;
	
	public OpenSession() {
		super("OpenSession", operationCode, TransactionID.zero, new ArrayList<>(1));
		sessionID = new SessionID();
		getArgs().add(sessionID);
	}
}
