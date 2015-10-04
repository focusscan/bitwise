package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int16 extends Datatype {
	public static final Int16 zero = new Int16((short) 0);
	public static final DatatypeDecoder<Int16> decoder = new DatatypeDecoder<Int16>() {
		@Override
		public Int16 getSample() {
			return zero;
		}

		@Override
		public Int16 decode(ByteBuffer in) {
			return new Int16(in);
		}
	};
	
	private short value;
	
	public Int16(ByteBuffer in) {
		super((short) 0x0003);
		int v0 = 0xff & in.get();
		int v1 = 0xff & in.get();
		value = (short) (v0 | (v1 << 8));
	}
	
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
		stream.write(v0);
		stream.write(v1);
	}
}
