package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.types.GenericContainer;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class BaseOperation<R extends BaseResponse> extends GenericContainer implements Operation {
	private final String operationName;
	private final List<Int32> args;
	
	public BaseOperation(String in_operationName, UInt16 in_operationCode, TransactionID in_transactionID, int arity) {
		super(new UInt32(arity * 4), GenericContainer.containerTypeCommand, in_operationCode, in_transactionID);
		operationName = in_operationName;
		args = new ArrayList<>(arity);
		assert(null != operationName);
	}
	
	public abstract R decodeResponse(ByteBuffer in);
	
	public String getOperationName() {
		return operationName;
	}
	
	public List<Int32> getArgs() {
		return args;
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		super.serialize(stream);
		for (Int32 arg : args)
			arg.serialize(stream);
	}
	
	public boolean serializeData(ByteArrayOutputStream stream) {
		return false;
	}
}
