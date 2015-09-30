package bitwise.devices.kinds;

import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriverID;

public interface DeviceKind {
	public UsbDriverID getDriverID();
	public UsbDevice getDevice();
}
