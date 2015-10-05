package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class BaseOperation<R extends BaseResponse> implements Operation {
	private static final UInt16 containerTypeCommand = new UInt16((short) 1);
	
	private final String operationName;
	private final UInt16 operationCode;
	private final TransactionID transactionID;
	private final List<Int32> args;
	
	public BaseOperation(String in_operationName, UInt16 in_operationCode, TransactionID in_transactionID, List<Int32> in_args) {
		operationName = in_operationName;
		operationCode = in_operationCode;
		args = in_args;
		transactionID = in_transactionID;
		assert(null != operationName);
		assert(null != operationCode);
		assert(null != args);
		assert(null != transactionID);
	}
	
	public abstract R decodeResponse(ByteBuffer in);
	
	@Override
	public UInt16 getOperationCode() {
		return operationCode;
	}
	
	@Override
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	public List<Int32> getArgs() {
		return args;
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
