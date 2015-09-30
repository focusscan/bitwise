package bitwise.devices.usb.drivers.nikon.d7200;

import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriver;

public class NikonD7200 extends UsbDriver implements FullCamera {
	public NikonD7200(UsbDevice in_device) {
		super(in_device);
	}

	@Override
	public String getName() {
		return NikonD7200Factory.driverName;
	}
}
