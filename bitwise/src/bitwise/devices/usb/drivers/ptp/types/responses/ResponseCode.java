package bitwise.devices.usb.drivers.ptp.types.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.Code;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class ResponseCode extends Code {
	public static final Decoder<ResponseCode> decoder = new Decoder<ResponseCode>() {
		@Override
		public ResponseCode decode(ByteBuffer in) {
			return new ResponseCode(in);
		}
	};
	
	protected ResponseCode(short in_value) {
		super(in_value);
	}
	
	protected ResponseCode(ByteBuffer in) {
		super(in);
	}
}
