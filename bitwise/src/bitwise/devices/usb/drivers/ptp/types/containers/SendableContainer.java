package bitwise.devices.usb.drivers.ptp.types.containers;

import java.io.ByteArrayOutputStream;

public interface SendableContainer {
	public void serialize(ByteArrayOutputStream stream);
}
