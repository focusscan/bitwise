package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public abstract class Code extends UInt16 {
	protected Code(ByteBuffer in) {
		super(in);
	}
	
	protected Code(short in_value) {
		super(in_value);
	}
}
