package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.prim.Unused;

public abstract class BaseOperation<A1 extends Int32, A2 extends Int32, A3 extends Int32, A4 extends Int32, A5 extends Int32> implements Operation {
	private static final UInt16 containerTypeCommand = new UInt16((short) 1);
	
	private final UInt16 operationCode;
	private final SessionID sessionID;
	private final TransactionID transactionID;
	private final A1 arg1;
	private final A2 arg2;
	private final A3 arg3;
	private final A4 arg4;
	private final A5 arg5;
	
	public BaseOperation(UInt16 in_operationCode, SessionID in_sessionID, A1 in_arg1, A2 in_arg2, A3 in_arg3, A4 in_arg4, A5 in_arg5) {
		operationCode = in_operationCode;
		if (null == in_sessionID) {
			sessionID = SessionID.zero;
			transactionID = TransactionID.zero;
		}
		else {
			sessionID = in_sessionID;
			transactionID = new TransactionID();
		}
		arg1 = in_arg1;
		arg2 = in_arg2;
		arg3 = in_arg3;
		arg4 = in_arg4;
		arg5 = in_arg5;
		assert(null != operationCode);
		assert(null != arg1);
		assert(null != arg2);
		assert(null != arg3);
		assert(null != arg4);
		assert(null != arg5);
	}
	
	@Override
	public UInt16 getOperationCode() {
		return operationCode;
	}
	
	@Override
	public SessionID getSessionID() {
		return sessionID;
	}
	
	@Override
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	protected A1 getArg1() {
		return arg1;
	}
	
	protected A2 getArg2() {
		return arg2;
	}
	
	protected A3 getArg3() {
		return arg3;
	}
	
	protected A4 getArg4() {
		return arg4;
	}
	
	protected A5 getArg5() {
		return arg5;
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		ByteArrayOutputStream cmd = new ByteArrayOutputStream();
		argSerial: do {
			if (arg1 instanceof Unused)
				break argSerial;
			arg1.serialize(cmd);
			if (arg2 instanceof Unused)
				break argSerial;
			arg2.serialize(cmd);
			if (arg3 instanceof Unused)
				break argSerial;
			arg3.serialize(cmd);
			if (arg4 instanceof Unused)
				break argSerial;
			arg4.serialize(cmd);
			if (arg5 instanceof Unused)
				break argSerial;
			arg5.serialize(cmd);
		} while (false);
		byte[] cmdBytes = cmd.toByteArray();
		
		UInt32 length = new UInt32(cmdBytes.length + 12);
		length.serialize(stream);
		containerTypeCommand.serialize(stream);		
		operationCode.serialize(stream);
		transactionID.serialize(stream);
	}
}
