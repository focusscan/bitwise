package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Str implements PtpType {
	public static final int MaxStrLength = 254;
	public static final Decoder<Str> decoder = new Decoder<Str>() {
		@Override
		public Str decode(ByteBuffer in) {
			return new Str(in);
		}
	};
	
	public static class StrTooLong extends Exception {
		private static final long serialVersionUID = -8164534523601745004L;
		private final String string;
		
		public StrTooLong(String in_string) {
			string = in_string;
			assert(null != in_string);
		}
		
		public String getString() {
			return string;
		}
	}
	
	private String value;
	
	public Str(ByteBuffer in) {
		int length = 0xff & in.get();
		if (0 == length)
			value = "";
		else {
			byte[] str = new byte[(length-1) * 2];
			in.get(str);
			value = new String(str, StandardCharsets.UTF_16LE);
			// Consume the null terminator
			in.get();
			in.get();
		}
	}
	
	public Str(String in_value) throws StrTooLong {
		setValue(in_value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String in_value) throws StrTooLong {
		if (null == in_value)
			value = "";
		else if (in_value.length() > MaxStrLength)
			throw new StrTooLong(in_value);
		else
			value = in_value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Str))
			return false;
		Str that = (Str)o;
		return this.value.equals(that.value);
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public void serialize(ByteArrayOutputStream stream) {
		byte len = (byte)value.length();
		stream.write(len);
		if (0 < len) {
			byte[] encodedString = value.getBytes(StandardCharsets.UTF_16LE);
			for (byte b : encodedString)
				stream.write(b);
			// Add null termination
			stream.write(0);
			stream.write(0);
		}
	}
}
