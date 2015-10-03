package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public abstract class Datatype {
	private final short datatypeCode;
	
	public Datatype(short in_datatypeCode) {
		datatypeCode = in_datatypeCode;
	}
	
	public short getDatatypeCode() {
		return datatypeCode;
	}
	
	public abstract void serialize(ByteArrayOutputStream stream);
}
