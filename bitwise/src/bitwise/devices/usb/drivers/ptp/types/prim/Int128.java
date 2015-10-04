package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int128 extends Datatype {
	public static final Int128 zero = new Int128(0, 0);
	public static final DatatypeDecoder<Int128> decoder = new DatatypeDecoder<Int128>() {
		@Override
		public Int128 getSample() {
			return zero;
		}

		@Override
		public Int128 decode(ByteBuffer in) {
			return new Int128(in);
		}
	};
	
	private long value_lo;
	private long value_hi;
	
	public Int128(ByteBuffer in) {
		super((short) 0x0009);
		long v0_lo = 0xff & in.get();
		long v1_lo = 0xff & in.get();
		long v2_lo = 0xff & in.get();
		long v3_lo = 0xff & in.get();
		long v4_lo = 0xff & in.get();
		long v5_lo = 0xff & in.get();
		long v6_lo = 0xff & in.get();
		long v7_lo = 0xff & in.get();
		long v0_hi = 0xff & in.get();
		long v1_hi = 0xff & in.get();
		long v2_hi = 0xff & in.get();
		long v3_hi = 0xff & in.get();
		long v4_hi = 0xff & in.get();
		long v5_hi = 0xff & in.get();
		long v6_hi = 0xff & in.get();
		long v7_hi = 0xff & in.get();
		value_lo = v0_lo
				 | (v1_lo << 8)
				 | (v2_lo << 16)
				 | (v3_lo << 24)
				 | (v4_lo << 32)
				 | (v5_lo << 40)
				 | (v6_lo << 48)
				 | (v7_lo << 56);
		value_hi = v0_hi
				 | (v1_hi << 8)
				 | (v2_hi << 16)
				 | (v3_hi << 24)
				 | (v4_hi << 32)
				 | (v5_hi << 40)
				 | (v6_hi << 48)
				 | (v7_hi << 56);
	}
	
	public Int128(long in_value_hi, long in_value_lo) {
		super((short) 0x0009);
		value_lo = in_value_lo;
		value_hi = in_value_hi;
	}
	
	public long getValueLo() {
		return value_lo;
	}
	
	public long getValueHi() {
		return value_hi;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Int128))
			return false;
		Int128 that = (Int128)o;
		return (this.value_lo == that.value_lo) && (this.value_hi == that.value_hi);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		byte v7_hi = (byte) (value_hi >> 56);
		byte v6_hi = (byte) (value_hi >> 48);
		byte v5_hi = (byte) (value_hi >> 40);
		byte v4_hi = (byte) (value_hi >> 32);
		byte v3_hi = (byte) (value_hi >> 24);
		byte v2_hi = (byte) (value_hi >> 16);
		byte v1_hi = (byte) (value_hi >> 8);
		byte v0_hi = (byte) value_hi;
		byte v7_lo = (byte) (value_lo >> 56);
		byte v6_lo = (byte) (value_lo >> 48);
		byte v5_lo = (byte) (value_lo >> 40);
		byte v4_lo = (byte) (value_lo >> 32);
		byte v3_lo = (byte) (value_lo >> 24);
		byte v2_lo = (byte) (value_lo >> 16);
		byte v1_lo = (byte) (value_lo >> 8);
		byte v0_lo = (byte) value_lo;
		stream.write(v0_lo);
		stream.write(v1_lo);
		stream.write(v2_lo);
		stream.write(v3_lo);
		stream.write(v4_lo);
		stream.write(v5_lo);
		stream.write(v6_lo);
		stream.write(v7_lo);
		stream.write(v0_hi);
		stream.write(v1_hi);
		stream.write(v2_hi);
		stream.write(v3_hi);
		stream.write(v4_hi);
		stream.write(v5_hi);
		stream.write(v6_hi);
		stream.write(v7_hi);
	}
}
