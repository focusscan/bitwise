package bitwise.devices.usbservice;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;
import bitwise.engine.Thing;

public abstract class UsbDriverFactory<H extends BaseDriverHandle<?, ?>> extends Thing<UsbDriverFactoryID> {
	protected UsbDriverFactory() {
		super(new UsbDriverFactoryID());
	}
	
	public abstract Class<H> getHandleClass();
	public abstract Class<? extends BaseDriver<UsbDevice, H>> getDriverClass();
	public abstract String getDriverName();
	
	public abstract boolean isCompatibleWith(UsbDevice device);
	
	public final BaseDriver<UsbDevice, H> makeDriver(UsbServiceCertificate cert, UsbDevice device) {
		if (null == cert)
			throw new IllegalArgumentException("UsbServiceCertificate");
		return doMakeDriver(device);
	}
	
	public abstract BaseDriver<UsbDevice, H> doMakeDriver(UsbDevice device);
}
