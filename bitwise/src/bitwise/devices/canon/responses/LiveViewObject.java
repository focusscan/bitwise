package bitwise.devices.canon.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class LiveViewObject {
	public final byte[] jpeg;
	
	public LiveViewObject(UsbPtpBuffer buf) throws UsbPtpCoderException {
		/* int len_in_bytes = */ buf.getInt();
		buf.getInt();
		
		jpeg = buf.getRemainingArray();
	}
}
