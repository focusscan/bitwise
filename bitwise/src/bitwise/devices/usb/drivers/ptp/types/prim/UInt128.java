package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt128 extends Int128 {
	public static final UInt128 zero = new UInt128(0, 0);
	public static final Decoder<UInt128> decoder = new Decoder<UInt128>() {
		@Override
		public UInt128 decode(ByteBuffer in) {
			return new UInt128(in);
		}
	};
	public static final Decoder<Arr<UInt128>> arrayDecoder = new Decoder<Arr<UInt128>>() {
		@Override
		public Arr<UInt128> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	public UInt128(ByteBuffer in) {
		super(in);
	}
	
	public UInt128(long in_value_hi, long in_value_lo) {
		super(in_value_hi, in_value_lo);
	}
}
