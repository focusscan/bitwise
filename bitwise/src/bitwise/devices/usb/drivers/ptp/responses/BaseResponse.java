package bitwise.devices.usb.drivers.ptp.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class BaseResponse implements Response {
	private final UInt32 length;
	private final UInt16 type;
	private final UInt16 code;
	private final TransactionID transactionID;
	
	public BaseResponse(ByteBuffer in) {
		length = new UInt32(in);
		type = new UInt16(in);
		code = new UInt16(in);
		transactionID = new TransactionID(in);
	}
	
	@Override
	public int getLength() {
		return length.getValue();
	}
	
	@Override
	public UInt16 getType() {
		return type;
	}
	
	@Override
	public UInt16 getCode() {
		return code;
	}
	
	@Override
	public TransactionID getTransactionID() {
		return transactionID;
	}
}
