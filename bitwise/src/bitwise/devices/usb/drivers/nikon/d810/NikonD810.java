package bitwise.devices.usb.drivers.nikon.d810;

import bitwise.apps.App;
import bitwise.devices.usb.drivers.nikon.NikonBase;

public class NikonD810 extends NikonBase {
	public NikonD810(App in_app) {
		super(in_app);
	}

	@Override
	public String getName() {
		return NikonD810Factory.driverName;
	}
}
