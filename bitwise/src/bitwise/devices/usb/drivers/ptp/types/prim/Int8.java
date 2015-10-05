package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Int8 extends IntegralType {
	public static final Int8 zero = new Int8((byte) 0);
	public static final Decoder<Int8> decoder = new Decoder<Int8>() {
		@Override
		public Int8 decode(ByteBuffer in) {
			return new Int8(in);
		}
	};
	public static final Decoder<Arr<Int8>> arrayDecoder = new Decoder<Arr<Int8>>() {
		@Override
		public Arr<Int8> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	private byte value;
	
	public Int8(ByteBuffer in) {
		value = in.get();
	}
	
	public Int8(byte in_value) {
		value = in_value;
	}
	
	public byte getValue() {
		return value;
	}
	
	public Int32 asInt32() {
		return new Int32(0xff & value);
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
	public String toString() {
		return String.format("%02x", value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		stream.write(value);
	}
}
