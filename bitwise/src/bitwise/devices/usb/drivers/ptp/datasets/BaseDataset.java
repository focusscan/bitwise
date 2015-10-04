package bitwise.devices.usb.drivers.ptp.datasets;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class BaseDataset {
	private final UInt32 length;
	private final UInt16 type;
	private final OperationCode operationCode;
	private final TransactionID transactionID;
	
	public BaseDataset(ByteBuffer in) {
		length = new UInt32(in);
		type = new UInt16(in);
		operationCode = new OperationCode(in);
		transactionID = new TransactionID(in);
	}
	
	public int getLength() {
		return length.getValue();
	}
	
	public UInt16 getType() {
		return type;
	}
	
	public OperationCode getOperationCode() {
		return operationCode;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
}
