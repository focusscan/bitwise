package bitwise.devices.usbptpcamera.events;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class Event {
	private final short eventCode;
	private final int transactionID;
	private final int[] arguments;
	
	public Event(UsbPtpBuffer buf, int argSize) throws UsbPtpCoderException {
		eventCode = buf.getShort();
		transactionID = buf.getInt();
		arguments = new int[argSize / 4];
		for (int i = 0; i < arguments.length; i++)
			arguments[i] = buf.getInt();
	}
	
	public short getEventCode() {
		return eventCode;
	}
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public int[] getArguments() {
		return arguments;
	}
}
