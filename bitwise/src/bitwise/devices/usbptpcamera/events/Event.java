package bitwise.devices.usbptpcamera.events;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class Event {
	private final short eventCode;
	private final int transactionID;
	private final UsbPtpBuffer data;
	
	public Event(UsbPtpBuffer buf) throws UsbPtpCoderException {
		eventCode = buf.getShort();
		transactionID = buf.getInt();
		data = buf;
	}
	
	public short getEventCode() {
		return eventCode;
	}
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public UsbPtpBuffer getData() {
		return data;
	}
}
