package bitwise.devices.usb.drivers.ptp.types.prim;

public class Unused extends Int32 {
	public static final Unused instance = new Unused(0);
	
	private Unused(int in_value) {
		super(in_value);
	}
}
