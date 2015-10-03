package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public class Undef extends Datatype {

	public Undef() {
		super((short) 0x0000);
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Undef);
	}

	@Override
	public void serialize(ByteArrayOutputStream stream) {
	}
}
