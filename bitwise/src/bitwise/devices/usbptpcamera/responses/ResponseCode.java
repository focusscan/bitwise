package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class ResponseCode extends Response {
	public static final int success = (short) 0x2001;
	public static final int deviceBusy = (short) 0x2019;
	
	private final short responseCode;
	private final int transactionID;
	private final int[] arguments;
	
	public ResponseCode(UsbPtpBuffer buf, int argSize) throws UsbPtpCoderException {
		arguments = new int[argSize / 4];
		responseCode = buf.getShort();
		transactionID = buf.getInt();
		for (int i = 0; i < arguments.length; i++)
			arguments[i] = buf.getInt();
	}
	
	public short getResponseCode() {
		return responseCode;
	}
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public int[] getArguments() {
		return arguments;
	}
}
