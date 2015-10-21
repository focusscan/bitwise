package bitwise.devices.usbptpcamera.coder;

public class Str extends UsbPtpPrimType {
	public final String value;
	public Str(String in) {
		value = (null == in) ? "" : in;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Str))
			return false;
		Str that = (Str) o;
		return this.value.equals(that.value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public void encode(UsbPtpBuffer buf) throws UsbPtpCoderException {
		buf.put(value);
	}
}
