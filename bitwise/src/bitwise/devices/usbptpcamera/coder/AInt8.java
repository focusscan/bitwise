package bitwise.devices.usbptpcamera.coder;

public class AInt8 implements UsbPtpPrimType {
	public final byte[] value;
	public AInt8(byte[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
