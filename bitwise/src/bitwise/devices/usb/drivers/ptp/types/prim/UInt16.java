package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public class UInt16 extends Int16 {
	public static final UInt16 zero = new UInt16((short) 0);
	public static final DatatypeDecoder<UInt16> decoder = new DatatypeDecoder<UInt16>() {
		@Override
		public UInt16 getSample() {
			return zero;
		}

		@Override
		public UInt16 decode(ByteBuffer in) {
			return new UInt16(in);
		}
	};
	
	public UInt16(ByteBuffer in) {
		super(in);
	}
	
	public UInt16(short in_value) {
		super(in_value);
	}
}
