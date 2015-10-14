package bitwise.devices.usbptpcamera.coder;

public class AInt16 implements UsbPtpPrimType {
	public final short[] value;
	public AInt16(short[] in) {
		value = in;
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
