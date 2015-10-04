package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.DatatypeDecoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class DevicePropCode extends UInt16 {
	public static final DevicePropCode zero = new DevicePropCode((short) 0);
	public static final DatatypeDecoder<DevicePropCode> decoder = new DatatypeDecoder<DevicePropCode>() {
		@Override
		public DevicePropCode getSample() {
			return zero;
		}

		@Override
		public DevicePropCode decode(ByteBuffer in) {
			return new DevicePropCode(in);
		}
	};
	
	public DevicePropCode(ByteBuffer in) {
		super(in);
	}
	
	public DevicePropCode(short in_value) {
		super(in_value);
	}
}
