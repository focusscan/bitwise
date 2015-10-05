package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class OperationCode extends UInt16 {
	public static final Decoder<OperationCode> decoder = new Decoder<OperationCode>() {
		@Override
		public OperationCode decode(ByteBuffer in) {
			return new OperationCode(in);
		}
	};
	
	public OperationCode(ByteBuffer in) {
		super(in);
	}
	
	public OperationCode(short in_value) {
		super(in_value);
	}
}
