package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public interface Operation {
	public UInt16 getCode();
	public TransactionID getTransactionID();
	public void serialize(ByteArrayOutputStream stream);
}
