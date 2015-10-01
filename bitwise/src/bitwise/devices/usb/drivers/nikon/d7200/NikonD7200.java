package bitwise.devices.usb.drivers.nikon.d7200;

import bitwise.apps.App;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.ptp.PTPCamera;

public class NikonD7200 extends PTPCamera {
	public NikonD7200(App in_app, UsbDevice in_device) {
		super(in_app, in_device);
	}

	@Override
	public String getName() {
		return NikonD7200Factory.driverName;
	}
}
