package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.UsbPtpBuffer;

public class ResponseData extends Response {
	private final short operationCode;
	private final int transactionID;
	private final UsbPtpBuffer data;
	
	public ResponseData(UsbPtpBuffer buf, int dataSize) {
		operationCode = buf.getShort();
		transactionID = buf.getInt();
		data = buf;
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
}
