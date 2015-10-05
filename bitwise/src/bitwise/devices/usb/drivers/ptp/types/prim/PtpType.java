package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public interface PtpType {
	public void serialize(ByteArrayOutputStream stream);
}
