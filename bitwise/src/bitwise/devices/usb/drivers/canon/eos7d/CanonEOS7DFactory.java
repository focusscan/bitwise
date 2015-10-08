package bitwise.devices.usb.drivers.canon.eos7d;

import bitwise.apps.App;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.UsbDriverFactory;

public class CanonEOS7DFactory extends UsbDriverFactory<CanonEOS7D> {
	protected static final String driverName = "Bitwise Canon EOS 7D";
	
	private static final CanonEOS7DFactory factory = new CanonEOS7DFactory();
	public static CanonEOS7DFactory getInstance() {
		return factory;
	}
	
	private CanonEOS7DFactory() {
		super();
	}

	@Override
	public boolean isCompatibleWith(UsbDevice in) {
		return (0x04a9 == in.getVendorID() && 0x319a == in.getProductID());
	}

	@Override
	protected CanonEOS7D makeDriver(App in_app, UsbDevice in_device) {
		if (!isCompatibleWith(in_device))
			return null;
		return new CanonEOS7D(in_app);
	}

	@Override
	public String getName() {
		return driverName;
	}
	
	@Override
	public Class<CanonEOS7D> getDriverClass() {
		return CanonEOS7D.class;
	}
}
