package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt64 extends Int64 {
	public static final UInt64 zero = new UInt64(0);
	public static final Decoder<UInt64> decoder = new Decoder<UInt64>() {
		@Override
		public UInt64 decode(ByteBuffer in) {
			return new UInt64(in);
		}
	};
	public static final Decoder<Arr<UInt64>> arrayDecoder = new Decoder<Arr<UInt64>>() {
		@Override
		public Arr<UInt64> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	public UInt64(ByteBuffer in) {
		super(in);
	}
	
	public UInt64(long in_value) {
		super(in_value);
	}
}
