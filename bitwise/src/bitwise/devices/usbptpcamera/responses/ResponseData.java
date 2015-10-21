package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class ResponseData extends Response {
	private final short operationCode;
	private final int transactionID;
	private final UsbPtpBuffer data;
	private final int dataSize;
	
	public ResponseData(UsbPtpBuffer buf, int in_dataSize) throws UsbPtpCoderException {
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
		return new UsbPtpBuffer(data);
	}
	
	public int getDataSize() {
		return dataSize;
	}
}
