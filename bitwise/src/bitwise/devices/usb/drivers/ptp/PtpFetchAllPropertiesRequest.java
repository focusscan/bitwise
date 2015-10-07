package bitwise.devices.usb.drivers.ptp;

import java.util.ArrayList;
import java.util.Collection;

import bitwise.apps.Resource;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbRequest;

public class PtpFetchAllPropertiesRequest extends UsbRequest {
	private final static ArrayList<Resource> emptyResource = new ArrayList<>(0);
	
	private final PtpCamera driver;
	
	public PtpFetchAllPropertiesRequest(PtpCamera in_driver) {
		super("PtpFetchAllPropertiesRequest");
		driver = in_driver;
		assert(null != driver);
	}
	
	public PtpCamera getDriver() {
		return driver;
	}
	
	@Override
	public Collection<Resource> getNewResources() {
		return emptyResource;
	}

	@Override
	protected synchronized void serveRequest(UsbContext ctx) throws InterruptedException {
		if (getRequestCanceled().get())
			return;
		driver.cmd_fetchAllCameraProperties();
	}
}
