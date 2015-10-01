package bitwise.devices.usb.drivers.ptp;

import bitwise.apps.App;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriver;

public abstract class PTPCamera extends UsbDriver implements FullCamera {
	public PTPCamera(App in_app, UsbDevice in_device) {
		super(in_app, in_device);
	}
	
	
}
