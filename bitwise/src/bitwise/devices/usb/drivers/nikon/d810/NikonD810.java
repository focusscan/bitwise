package bitwise.devices.usb.drivers.nikon.d810;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;

import bitwise.apps.App;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.nikon.NikonBase;

public class NikonD810 extends NikonBase {
	public NikonD810(App in_app, UsbDevice in_device) {
		super(in_app, in_device);
	}

	@Override
	public String getName() {
		return NikonD810Factory.driverName;
	}
	
	@Override
	public boolean initialize() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		if (!super.initialize())
			return false;
		// TODO
		return true;
	}
}
