package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.DatatypeDecoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class ObjectFormatCode extends UInt16 {
	public static final ObjectFormatCode zero = new ObjectFormatCode((short) 0);
	public static final DatatypeDecoder<ObjectFormatCode> decoder = new DatatypeDecoder<ObjectFormatCode>() {
		@Override
		public ObjectFormatCode getSample() {
			return zero;
		}

		@Override
		public ObjectFormatCode decode(ByteBuffer in) {
			return new ObjectFormatCode(in);
		}
	};
	
	public ObjectFormatCode(ByteBuffer in) {
		super(in);
	}
	
	public ObjectFormatCode(short in_value) {
		super(in_value);
	}
}
