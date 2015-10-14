package bitwise.devices.usbptpcamera.coder;

public class Int16 implements UsbPtpPrimType {
	public final short value;
	public Int16(short in) {
		value = in;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int16))
			return false;
		Int16 that = (Int16) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%04x", value);
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
