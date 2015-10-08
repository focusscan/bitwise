package bitwise.devices.usb.drivers.canon.eos7d;

import bitwise.apps.App;
import bitwise.devices.usb.drivers.canon.CanonBase;

public class CanonEOS7D extends CanonBase {
	public CanonEOS7D(App in_app) {
		super(in_app);
	}

	@Override
	public String getName() {
		return CanonEOS7DFactory.driverName;
	}
}
