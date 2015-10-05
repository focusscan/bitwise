package bitwise.devices.usb.drivers.ptp.operations;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class CloseSession extends Operation {
	public static final UInt16 operationCode = new UInt16((short) 0x1003);
	
	public CloseSession() {
		super("CloseSession", operationCode, 0, null);
	}
}
