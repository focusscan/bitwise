package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public abstract class Operation<R> {
	private final String operationName;
	private final OperationCode code;
	private TransactionID transactionID = null;
	private final List<Int32> arguments;
	private final PtpType outData;
	
	public Operation(String in_operationName, TransactionID in_transactionID, OperationCode in_code, int arity, PtpType in_outData) {
		operationName = in_operationName;
		code = in_code;
		transactionID = in_transactionID;
		arguments = new ArrayList<>(arity);
		outData = in_outData;
	}
	
	public Operation(String in_operationName, OperationCode in_code, int arity, PtpType in_outData) {
		operationName = in_operationName;
		code = in_code;
		arguments = new ArrayList<>(arity);
		outData = in_outData;
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	public OperationCode getCode() {
		return code;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	public void setTransactionID(TransactionID in) {
		if (null == transactionID)
			transactionID = in;
	}
	
	public List<Int32> getArguments() {
		return arguments;
	}
	
	public PtpType getOutData() {
		return outData;
	}
	
	private UInt16 responseCode = null;
	
	public UInt16 getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(UInt16 in) {
		if (null == responseCode)
			responseCode = in;
	}
	
	private List<Int32> responseArguments = null;
	
	public List<Int32> getResponseArguments() {
		return responseArguments;
	}
	
	public void setResponseArguments(ByteBuffer in) {
		if (null == responseArguments) {
			int numArgs = in.remaining() / 4;
			responseArguments = new ArrayList<Int32>(numArgs);
			for (int i = 0; i < numArgs; i++)
				responseArguments.add(Int32.decoder.decode(in));
		}
	}
	
	public R getResponseData() {
		return null;
	}
	
	public boolean setResponseData(ByteBuffer in) {
		return false;
	}
}
