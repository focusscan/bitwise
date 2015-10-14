package bitwise.devices.usbptpcamera.events;

import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpDecoder;

public class EventDecoder implements UsbPtpDecoder<Event> {
	public EventDecoder() {
		
	}
	
	@Override
	public Event decode(UsbPtpBuffer in) throws UsbPtpCoderException {
		int length = in.getInt();
		final short type = in.getShort();
		if (type == BaseUsbPtpCamera.containerCodeEvent)
			return new Event(in, length - 12);
		return null;
	}
}
