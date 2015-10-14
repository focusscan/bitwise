package bitwise.devices.usbptpcamera.coder;

public class Int8 extends UsbPtpPrimType {
	public final byte value;
	public Int8(byte in) {
		value = in;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int8))
			return false;
		Int8 that = (Int8) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%02x", value);
	}

	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
