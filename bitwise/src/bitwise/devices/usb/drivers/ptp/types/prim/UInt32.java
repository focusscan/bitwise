package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.canon.types.CanonArr;

public class UInt32 extends Int32 {
	public static final UInt32 zero = new UInt32(0);
	public static final Decoder<UInt32> decoder = new Decoder<UInt32>() {
		@Override
		public UInt32 decode(ByteBuffer in) {
			return new UInt32(in);
		}
	};
	public static final Decoder<Arr<UInt32>> arrayDecoder = new Decoder<Arr<UInt32>>() {
		@Override
		public Arr<UInt32> decode(ByteBuffer in) {
			return new Arr<>(decoder, in);
		}
	};
	
	public static final Decoder<CanonArr> canonArrayDecoder = new Decoder<CanonArr>() {
		@Override
		public CanonArr decode(ByteBuffer in) {
			return new CanonArr(decoder, in);
		}
	};
	
	public UInt32(ByteBuffer in) {
		super(in);
	}
	
	public UInt32(int in_value) {
		super(in_value);
	}
}
