package bitwise.devices.usbptpcamera.coder;

public class AInt64 implements UsbPtpPrimType {
	public final long[] value;
	public AInt64(long[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
