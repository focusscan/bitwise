package bitwise.devices.usbptpcamera.coder;

public class AInt32 extends UsbPtpPrimType {
	public final int[] value;
	public AInt32(int[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
