package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int64 extends Datatype {
	public static final Int64 zero = new Int64(0);
	public static final DatatypeDecoder<Int64> decoder = new DatatypeDecoder<Int64>() {
		@Override
		public Int64 getSample() {
			return zero;
		}

		@Override
		public Int64 decode(ByteBuffer in) {
			return new Int64(in);
		}
	};
	
	private long value;
	
	public Int64(ByteBuffer in) {
		super((short) 0x0007);
		long v0 = 0xff & in.get();
		long v1 = 0xff & in.get();
		long v2 = 0xff & in.get();
		long v3 = 0xff & in.get();
		long v4 = 0xff & in.get();
		long v5 = 0xff & in.get();
		long v6 = 0xff & in.get();
		long v7 = 0xff & in.get();
		value = v0
				| (v1 << 8)
				| (v2 << 16)
				| (v3 << 24)
				| (v4 << 32)
				| (v5 << 40)
				| (v6 << 48)
				| (v7 << 56);
	}
	
	public Int64(long in_value) {
		super((short) 0x0007);
		value = in_value;
	}
	
	public long getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int64))
			return false;
		Int64 that = (Int64)o;
		return (this.value == that.value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		byte v7 = (byte) (value >> 56);
		byte v6 = (byte) (value >> 48);
		byte v5 = (byte) (value >> 40);
		byte v4 = (byte) (value >> 32);
		byte v3 = (byte) (value >> 24);
		byte v2 = (byte) (value >> 16);
		byte v1 = (byte) (value >> 8);
		byte v0 = (byte) value;
		stream.write(v0);
		stream.write(v1);
		stream.write(v2);
		stream.write(v3);
		stream.write(v4);
		stream.write(v5);
		stream.write(v6);
		stream.write(v7);
	}
}
