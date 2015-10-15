package bitwise.devices.nikon.reponses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class LiveViewObject {
	public final byte[] jpeg;
	
	public LiveViewObject(UsbPtpBuffer buf) throws UsbPtpCoderException {
		for (int i = 0; i < 384; i++)
			buf.getByte();
		
		jpeg = buf.getRemainingArray();
	}
}
