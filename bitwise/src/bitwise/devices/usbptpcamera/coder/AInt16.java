package bitwise.devices.usbptpcamera.coder;

public class AInt16 extends UsbPtpPrimType {
	public final short[] value;
	public AInt16(short[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
