package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;

public class ResponseData extends Response {
	private final short operationCode;
	private final int transactionID;
	private final UsbPtpBuffer data;
	private final int dataSize;
	
	public ResponseData(UsbPtpBuffer buf, int in_dataSize) {
		operationCode = buf.getShort();
		transactionID = buf.getInt();
		data = buf;
		dataSize = in_dataSize;
	}
	
	public short getOperationCode() {
		return operationCode;
	}
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public UsbPtpBuffer getData() {
		return data;
	}
	
	public int getDataSize() {
		return dataSize;
	}
}
