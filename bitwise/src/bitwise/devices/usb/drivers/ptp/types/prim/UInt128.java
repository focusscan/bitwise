package bitwise.devices.usb.drivers.ptp.types.prim;

public class UInt128 extends Int128 {
	public static final UInt128 zero = new UInt128(0, 0);
	
	public UInt128(long in_value_hi, long in_value_lo) {
		super(in_value_hi, in_value_lo);
	}
}
