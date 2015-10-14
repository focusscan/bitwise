package bitwise.devices.usbptpcamera.events;

import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpDecoder;

public class EventDecoder implements UsbPtpDecoder<Event> {
	public EventDecoder() {
		
	}
	
	@Override
	public Event decode(UsbPtpBuffer in) {
		in.getInt(); /* length */
		final short type = in.getShort();
		if (type == BaseUsbPtpCamera.containerCodeEvent)
			return new Event(in);
		return null;
	}
}
