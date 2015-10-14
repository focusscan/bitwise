package bitwise.devices.usbptpcamera.coder;

public class Int64 implements UsbPtpPrimType {
	public final long value;
	public Int64(long in) {
		value = in;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int64))
			return false;
		Int64 that = (Int64) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%016x", value);
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) {
		buf.put(value);
	}
}
