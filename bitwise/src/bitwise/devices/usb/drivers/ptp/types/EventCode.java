package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.DatatypeDecoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class EventCode extends UInt16 {
	public static final EventCode zero = new EventCode((short) 0);
	public static final DatatypeDecoder<EventCode> decoder = new DatatypeDecoder<EventCode>() {
		@Override
		public EventCode getSample() {
			return zero;
		}

		@Override
		public EventCode decode(ByteBuffer in) {
			return new EventCode(in);
		}
	};
	
	public EventCode(ByteBuffer in) {
		super(in);
	}
	
	public EventCode(short in_value) {
		super(in_value);
	}
}
