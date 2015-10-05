package bitwise.devices.usb.drivers.ptp.responses;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public interface Response {
	public int getLength();
	public UInt16 getType();
	public UInt16 getCode();
	public TransactionID getTransactionID();
}
