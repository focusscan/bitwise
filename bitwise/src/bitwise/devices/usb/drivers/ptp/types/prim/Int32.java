package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int32 extends IntegralType {
	public static final Int32 zero = new Int32(0);
	public static final Decoder<Int32> decoder = new Decoder<Int32>() {
		@Override
		public Int32 decode(ByteBuffer in) {
			return new Int32(in);
		}
	};
	public static final Decoder<Arr<Int32>> arrayDecoder = new Decoder<Arr<Int32>>() {
		@Override
		public Arr<Int32> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	private int value;
	
	public Int32(ByteBuffer in) {
		int v0 = 0xff & in.get();
		int v1 = 0xff & in.get();
		int v2 = 0xff & in.get();
		int v3 = 0xff & in.get();
		value = v0 | (v1 << 8) | (v2 << 16) | (v3 << 24);
	}
	
	public Int32(int in_value) {
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
	public String toString() {
		return String.format("%08x", value);
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
