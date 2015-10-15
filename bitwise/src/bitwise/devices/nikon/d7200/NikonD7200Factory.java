package bitwise.devices.nikon.d7200;

import bitwise.devices.nikon.NikonHandle;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.log.Log;

public final class NikonD7200Factory extends UsbDriverFactory<NikonHandle> {
	private static NikonD7200Factory instance = null;
	public static NikonD7200Factory getInstance() {
		if (null == instance) {
			instance = new NikonD7200Factory();
			Log.log(instance, "Instance created");
		}
		return instance;
	}
	
	protected NikonD7200Factory() {
		
	}
	
	@Override
	public String getDriverName() {
		return "Bitwise Nikon D7200";
	}
	
	@Override
	public Class<NikonHandle> getHandleClass() {
		return NikonHandle.class;
	}
	
	@Override
	public Class<NikonD7200> getDriverClass() {
		return NikonD7200.class;
	}
	
	@Override
	public boolean isCompatibleWith(UsbDevice device) {
		final short vendorID  = 0x04b0;
		final short productID = 0x0439;
		return (device.getVendorID() == vendorID && device.getProductID() == productID);
	}
	
	@Override
	public NikonD7200 doMakeDriver(UsbDevice device) {
		return new NikonD7200(device);
	}
}
