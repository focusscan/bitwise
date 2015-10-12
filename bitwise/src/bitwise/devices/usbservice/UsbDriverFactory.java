package bitwise.devices.usbservice;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.engine.Thing;

public abstract class UsbDriverFactory<R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> extends Thing<UsbDriverFactoryID> {
	protected UsbDriverFactory() {
		super(new UsbDriverFactoryID());
	}
	
	public final A makeDriver(UsbServiceCertificate cert, UsbDevice device) {
		if (null == cert)
			throw new IllegalArgumentException("UsbServiceCertificate");
		return doMakeDriver(device);
	}
	
	public abstract A doMakeDriver(UsbDevice device);
}
