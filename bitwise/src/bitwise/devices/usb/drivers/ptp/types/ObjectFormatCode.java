package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class ObjectFormatCode extends UInt16 {
	public static final Decoder<ObjectFormatCode> decoder = new Decoder<ObjectFormatCode>() {
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
