package bitwise.devices.canon.eos7d;

import bitwise.devices.canon.CanonHandle;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.log.Log;

public class CanonEOS7DFactory extends UsbDriverFactory<CanonHandle> {
	private static CanonEOS7DFactory instance = null;

	public static CanonEOS7DFactory getInstance() {
		if (null == instance) {
			instance = new CanonEOS7DFactory();
			Log.log(instance, "Instance created");
		}
		return instance;
	}

	protected CanonEOS7DFactory() {
		
	}

	@Override
	public String getDriverName() {
		return "Bitwise Canon EOS 7D";
	}

	@Override
	public Class<CanonHandle> getHandleClass() {
		return CanonHandle.class;
	}

	@Override
	public Class<CanonEOS7D> getDriverClass() {
		return CanonEOS7D.class;
	}

	@Override
	public boolean isCompatibleWith(UsbDevice device) {
		final short vendorID = 0x04a9;
		final short productID = 0x319a;
		return (device.getVendorID() == vendorID && device.getProductID() == productID);
	}

	@Override
	public CanonEOS7D doMakeDriver(UsbDevice device) {
		return new CanonEOS7D(device);
	}
}
