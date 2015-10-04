package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;
import java.util.List;

import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class BaseOperation implements Operation {
	private static final UInt16 containerTypeCommand = new UInt16((short) 1);
	
	private final UInt16 operationCode;
	private final SessionID sessionID;
	private final TransactionID transactionID;
	private final List<Int32> args;
	
	public BaseOperation(UInt16 in_operationCode, SessionID in_sessionID, List<Int32> in_args) {
		operationCode = in_operationCode;
		args = in_args;
		if (null == in_sessionID) {
			sessionID = SessionID.zero;
			transactionID = TransactionID.zero;
		}
		else {
			sessionID = in_sessionID;
			transactionID = new TransactionID();
		}
		assert(null != operationCode);
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
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		UInt32 length = new UInt32(args.size() * 4 + 12);
		length.serialize(stream);
		containerTypeCommand.serialize(stream);		
		operationCode.serialize(stream);
		transactionID.serialize(stream);
		for (Int32 arg : args)
			arg.serialize(stream);
	}
}
