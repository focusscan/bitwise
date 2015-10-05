package bitwise.devices.usb.drivers.ptp.types;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class GenericContainer {
	public static final UInt16 containerTypeCommand = new UInt16((short) 1);
	public static final UInt16 containerTypeData = new UInt16((short) 2);
	public static final UInt16 containerTypeResponse = new UInt16((short) 3);
	public static final UInt16 containerTypeEvent = new UInt16((short) 4);
	
	private final UInt32 length;
	private final UInt16 type;
	private final UInt16 code;
	private final TransactionID transactionID;
	
	public GenericContainer(ByteBuffer in) {
		length = new UInt32(in);
		type = new UInt16(in);
		code = new UInt16(in);
		transactionID = new TransactionID(in);
	}
	
	public GenericContainer(UInt32 in_length, UInt16 in_type, UInt16 in_code, TransactionID in_transactionID) {
		length = in_length;
		type = in_type;
		code = in_code;
		transactionID = in_transactionID;
		assert(null != length);
		assert(null != type);
		assert(null != code);
		assert(null != transactionID);
	}
	
	public int getLength() {
		if (null == length)
			return 0;
		return length.getValue();
	}
	
	public UInt16 getType() {
		return type;
	}
	
	public UInt16 getCode() {
		return code;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	public void serialize(ByteArrayOutputStream stream) {
		UInt32 fullLength = new UInt32(12 + length.getValue());
		fullLength.serialize(stream);
		type.serialize(stream);
		code.serialize(stream);
		transactionID.serialize(stream);
	}
}
