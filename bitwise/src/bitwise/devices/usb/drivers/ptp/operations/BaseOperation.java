package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;

public abstract class BaseOperation<A1 extends Int32, A2 extends Int32, A3 extends Int32, A4 extends Int32, A5 extends Int32> implements Operation {
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
		operationCode.serialize(stream);
		sessionID.serialize(stream);
		transactionID.serialize(stream);
		arg1.serialize(stream);
		arg2.serialize(stream);
		arg3.serialize(stream);
		arg4.serialize(stream);
		arg5.serialize(stream);
	}
}
