package bitwise.devices.usb.drivers.ptp.types.prim;

public class UInt8 extends Int8 {
	public static final UInt8 zero = new UInt8((byte) 0);
	
	public UInt8(byte in_value) {
		super(in_value);
	}
}
