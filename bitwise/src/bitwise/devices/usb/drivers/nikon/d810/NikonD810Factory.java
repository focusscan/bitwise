package bitwise.devices.usb.drivers.nikon.d810;

import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriverFactory;

public class NikonD810Factory extends UsbDriverFactory<NikonD810> {
	protected static final String driverName = "Bitwise Nikon D810";
	
	private static final NikonD810Factory factory = new NikonD810Factory();
	public static NikonD810Factory getInstance() {
		return factory;
	}
	
	private NikonD810Factory() {
		super();
	}

	@Override
	public boolean isCompatibleWith(UsbDevice in) {
		return (0x04b0 == in.getVendorID() && 0x0436 == in.getProductID());
	}

	@Override
	protected NikonD810 makeDriver(UsbDevice in) {
		if (!isCompatibleWith(in))
			return null;
		return new NikonD810(in);
	}

	@Override
	public String getName() {
		return driverName;
	}
	
	@Override
	public Class<NikonD810> getDriverClass() {
		return NikonD810.class;
	}
}
