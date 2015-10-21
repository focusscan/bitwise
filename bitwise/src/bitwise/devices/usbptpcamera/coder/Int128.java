package bitwise.devices.usbptpcamera.coder;

public class Int128 extends UsbPtpPrimType {
	public final long value_lo;
	public final long value_hi;
	public Int128(long in_lo, long in_hi) {
		value_lo = in_lo;
		value_hi = in_hi;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int128))
			return false;
		Int128 that = (Int128) o;
		return this.value_lo == that.value_lo && this.value_hi == that.value_hi;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(value_lo) ^ Long.hashCode(value_hi);
	}
	
	@Override
	public String toString() {
		return String.format("%016x%016x", value_lo, value_hi);
	}
	
	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(this);
	}
}
