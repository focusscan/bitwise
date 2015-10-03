package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public class Int32 extends Datatype {
	public static final Int32 zero = new Int32(0);
	
	private int value;
	
	public Int32(int in_value) {
		super((short) 0x0005);
		value = in_value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int32))
			return false;
		Int32 that = (Int32)o;
		return (this.value == that.value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		byte v3 = (byte) (value >> 24);
		byte v2 = (byte) (value >> 16);
		byte v1 = (byte) (value >> 8);
		byte v0 = (byte) value;
		stream.write(v0);
		stream.write(v1);
		stream.write(v2);
		stream.write(v3);
	}
}
