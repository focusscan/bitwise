package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public class Int16 extends Datatype {
	public static final Int16 zero = new Int16((short) 0);
	
	private short value;
	
	public Int16(short in_value) {
		super((short) 0x0003);
		value = in_value;
	}
	
	public short getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int16))
			return false;
		Int16 that = (Int16)o;
		return (this.value == that.value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		byte v1 = (byte) (value >> 8);
		byte v0 = (byte) value;
		stream.write(v1);
		stream.write(v0);
	}
}
