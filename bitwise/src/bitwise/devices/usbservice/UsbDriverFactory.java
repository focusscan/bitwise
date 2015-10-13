package bitwise.devices.usbservice;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;
import bitwise.engine.Thing;

public abstract class UsbDriverFactory<H extends BaseDriverHandle<?, ?>, D extends BaseDriver<UsbDevice, H>> extends Thing<UsbDriverFactoryID> {
	protected UsbDriverFactory() {
		super(new UsbDriverFactoryID());
	}
	
	public abstract Class<H> getHandleClass();
	public abstract Class<D> getDriverClass();
	public abstract String getDriverName();
	
	public abstract boolean isCompatibleWith(UsbDevice device);
	
	public final D makeDriver(UsbServiceCertificate cert, UsbDevice device) {
		if (null == cert)
			throw new IllegalArgumentException("UsbServiceCertificate");
		return doMakeDriver(device);
	}
	
	public abstract D doMakeDriver(UsbDevice device);
}
