package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public interface Decoder<T> {
	public T decode(ByteBuffer in);
}
