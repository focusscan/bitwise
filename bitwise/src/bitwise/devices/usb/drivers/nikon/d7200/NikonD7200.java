package bitwise.devices.usb.drivers.nikon.d7200;

import bitwise.apps.App;
import bitwise.devices.usb.drivers.nikon.NikonBase;

public class NikonD7200 extends NikonBase {
	public NikonD7200(App in_app) {
		super(in_app);
	}

	@Override
	public String getName() {
		return NikonD7200Factory.driverName;
	}
}
