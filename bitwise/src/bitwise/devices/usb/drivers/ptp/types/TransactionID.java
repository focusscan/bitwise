package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class TransactionID extends UInt32 {
	public static final TransactionID zero = new TransactionID(0);
	
	private static int nextTransactionID = 1;
	private synchronized static int getNextTransactionID() {
		return nextTransactionID++;
	}
	
	public TransactionID(ByteBuffer in) {
		super(in);
	}
	
	public TransactionID(int in_value) {
		super(in_value);
	}
	
	public TransactionID() {
		super(getNextTransactionID());
	}
}
