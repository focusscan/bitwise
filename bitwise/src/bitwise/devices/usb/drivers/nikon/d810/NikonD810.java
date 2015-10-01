package bitwise.devices.usb.drivers.nikon.d810;

import bitwise.apps.App;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.ptp.PTPCamera;

public class NikonD810 extends PTPCamera {
	public NikonD810(App in_app, UsbDevice in_device) {
		super(in_app, in_device);
	}

	@Override
	public String getName() {
		return NikonD810Factory.driverName;
	}
}
