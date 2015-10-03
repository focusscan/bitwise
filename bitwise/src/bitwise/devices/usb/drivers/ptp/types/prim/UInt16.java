package bitwise.devices.usb.drivers.ptp.types.prim;

public class UInt16 extends Int16 {
	public static final UInt16 zero = new UInt16((short) 0);
	
	public UInt16(short in_value) {
		super(in_value);
	}
}
