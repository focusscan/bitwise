package bitwise.devices.usbptpcamera.coder;

public class AInt128 extends UsbPtpPrimType {
	public final Int128[] value;
	public AInt128(Int128[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
