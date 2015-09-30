package bitwise.devices.usb.drivers.nikon.d810;

import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriver;

public class NikonD810 extends UsbDriver implements FullCamera {
	public NikonD810(UsbDevice in_device) {
		super(in_device);
	}

	@Override
	public String getName() {
		return NikonD810Factory.driverName;
	}
}
