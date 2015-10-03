package bitwise.devices.usb.drivers.ptp.types;

import bitwise.devices.usb.drivers.ptp.types.prim.Str;

public class DateTime extends Str {
	public DateTime(String in_value) throws Str.StrTooLong {
		super(in_value);
	}
}
