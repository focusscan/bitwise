package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int8 extends Datatype {
	public static final Int8 zero = new Int8((byte) 0);
	public static final DatatypeDecoder<Int8> decoder = new DatatypeDecoder<Int8>() {
		@Override
		public Int8 getSample() {
			return zero;
		}

		@Override
		public Int8 decode(ByteBuffer in) {
			return new Int8(in);
		}
	};
	
	private byte value;
	
	public Int8(ByteBuffer in) {
		super((short) 0x0001);
		value = in.get();
	}
	
	public Int8(byte in_value) {
		super((short) 0x0001);
		value = in_value;
	}
	
	public byte getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int8))
			return false;
		Int8 that = (Int8)o;
		return (this.value == that.value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		stream.write(value);
	}
}
