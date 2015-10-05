package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt16 extends Int16 {
	public static final UInt16 zero = new UInt16((short) 0);
	public static final Decoder<UInt16> decoder = new Decoder<UInt16>() {
		@Override
		public UInt16 decode(ByteBuffer in) {
			return new UInt16(in);
		}
	};
	public static final Decoder<Arr<UInt16>> arrayDecoder = new Decoder<Arr<UInt16>>() {
		@Override
		public Arr<UInt16> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	public UInt16(ByteBuffer in) {
		super(in);
	}
	
	public UInt16(short in_value) {
		super(in_value);
	}
}
