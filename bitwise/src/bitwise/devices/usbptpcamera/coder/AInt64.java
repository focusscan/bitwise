package bitwise.devices.usbptpcamera.coder;

public class AInt64 extends UsbPtpPrimType {
	public final long[] value;
	public AInt64(long[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
