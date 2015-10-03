package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Str extends Datatype {
	public static final int MaxStrLength = 254;
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
	
	public Str(String in_value) throws StrTooLong {
		super((short) 0xffff);
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
	public void serialize(ByteArrayOutputStream stream) {
		byte len = (byte)value.length();
		byte[] encodedString = value.getBytes(StandardCharsets.UTF_16LE);
		stream.write(len);
		for (int i = 0; i < len; i++)
			stream.write(encodedString[i]);
	}
}
