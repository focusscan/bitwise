package bitwise.devices.usb.drivers.nikon.d7200;

import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriverFactory;

public class NikonD7200Factory extends UsbDriverFactory<NikonD7200> {
	protected static final String driverName = "Bitwise Nikon D7200";
	
	private static final NikonD7200Factory factory = new NikonD7200Factory();
	public static NikonD7200Factory getInstance() {
		return factory;
	}
	
	private NikonD7200Factory() {
		super();
	}

	@Override
	public boolean isCompatibleWith(UsbDevice in) {
		return (0x04b0 == in.getVendorID() && 0x0439 == in.getProductID());
	}

	@Override
	protected NikonD7200 makeDriver(UsbDevice in) {
		if (!isCompatibleWith(in))
			return null;
		return new NikonD7200(in);
	}

	@Override
	public String getName() {
		return driverName;
	}
	
	@Override
	public Class<NikonD7200> getDriverClass() {
		return NikonD7200.class;
	}
}
