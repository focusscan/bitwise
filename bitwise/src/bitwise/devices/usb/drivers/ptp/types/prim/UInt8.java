package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt8 extends Int8 {
	public static final UInt8 zero = new UInt8((byte) 0);
	public static final DatatypeDecoder<UInt8> decoder = new DatatypeDecoder<UInt8>() {
		@Override
		public UInt8 getSample() {
			return zero;
		}

		@Override
		public UInt8 decode(ByteBuffer in) {
			return new UInt8(in);
		}
	};
	
	public UInt8(ByteBuffer in) {
		super(in);
	}
	
	public UInt8(byte in_value) {
		super(in_value);
	}
}
