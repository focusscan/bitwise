package bitwise.devices.usb.drivers.ptp.types.prim;

import java.nio.ByteBuffer;

public interface DatatypeDecoder<D extends Datatype> {
	public D getSample();
	public D decode(ByteBuffer in);
}
