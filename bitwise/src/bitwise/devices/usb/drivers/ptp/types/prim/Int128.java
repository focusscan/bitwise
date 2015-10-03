package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public class Int128 extends Datatype {
	public static final Int128 zero = new Int128(0, 0);
	
	private long value_lo;
	private long value_hi;
	
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
		stream.write(v7_hi);
		stream.write(v6_hi);
		stream.write(v5_hi);
		stream.write(v4_hi);
		stream.write(v3_hi);
		stream.write(v2_hi);
		stream.write(v1_hi);
		stream.write(v0_hi);
		stream.write(v7_lo);
		stream.write(v6_lo);
		stream.write(v5_lo);
		stream.write(v4_lo);
		stream.write(v3_lo);
		stream.write(v2_lo);
		stream.write(v1_lo);
		stream.write(v0_lo);
	}
}
