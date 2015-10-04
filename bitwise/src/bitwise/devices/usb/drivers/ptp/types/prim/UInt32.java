package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt32 extends Int32 {
	public static final UInt32 zero = new UInt32(0);
	public static final DatatypeDecoder<UInt32> decoder = new DatatypeDecoder<UInt32>() {
		@Override
		public UInt32 getSample() {
			return zero;
		}

		@Override
		public UInt32 decode(ByteBuffer in) {
			return new UInt32(in);
		}
	};
	
	public UInt32(ByteBuffer in) {
		super(in);
	}
	
	public UInt32(int in_value) {
		super(in_value);
	}
}
