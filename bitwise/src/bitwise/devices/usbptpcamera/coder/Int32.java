package bitwise.devices.usbptpcamera.coder;

public class Int32 implements UsbPtpPrimType {
	public final int value;
	public Int32(int in) {
		value = in;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int32))
			return false;
		Int32 that = (Int32) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%08x", value);
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
