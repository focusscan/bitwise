package bitwise.devices.usbptpcamera.coder;

public class AInt32 implements UsbPtpPrimType {
	public final int[] value;
	public AInt32(int[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
