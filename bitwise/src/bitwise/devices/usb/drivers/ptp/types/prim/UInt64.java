package bitwise.devices.usb.drivers.ptp.types.prim;

public class UInt64 extends Int64 {
	public static final UInt64 zero = new UInt64(0);
	
	public UInt64(long in_value) {
		super(in_value);
	}
}
