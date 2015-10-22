package bitwise.devices.usbptpcamera.coder;

public class ByteLenAInt32 extends UsbPtpPrimType {
	public final int[] value;
	public ByteLenAInt32(int[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(this);
	}
}
