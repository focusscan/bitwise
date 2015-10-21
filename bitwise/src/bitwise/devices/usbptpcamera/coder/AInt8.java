package bitwise.devices.usbptpcamera.coder;

public class AInt8 extends UsbPtpPrimType {
	public final byte[] value;
	public AInt8(byte[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
