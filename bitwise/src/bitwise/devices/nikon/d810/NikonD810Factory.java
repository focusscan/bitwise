package bitwise.devices.nikon.d810;

import bitwise.devices.nikon.NikonHandle;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.log.Log;

public final class NikonD810Factory extends UsbDriverFactory<NikonHandle> {
	private static NikonD810Factory instance = null;
	public static NikonD810Factory getInstance() {
		if (null == instance) {
			instance = new NikonD810Factory();
			Log.log(instance, "Instance created");
		}
		return instance;
	}
	
	protected NikonD810Factory() {
		
	}
	
	@Override
	public String getDriverName() {
		return "Bitwise Nikon D810";
	}
	
	@Override
	public Class<NikonHandle> getHandleClass() {
		return NikonHandle.class;
	}
	
	@Override
	public Class<NikonD810> getDriverClass() {
		return NikonD810.class;
	}
	
	@Override
	public boolean isCompatibleWith(UsbDevice device) {
		final short vendorID  = 0x04b0;
		final short productID = 0x0436;
		return (device.getVendorID() == vendorID && device.getProductID() == productID);
	}
	
	@Override
	public NikonD810 doMakeDriver(UsbDevice device) {
		return new NikonD810(device);
	}
}
