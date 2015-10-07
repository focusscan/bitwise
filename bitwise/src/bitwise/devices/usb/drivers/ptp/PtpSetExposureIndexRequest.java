package bitwise.devices.usb.drivers.ptp;

import java.util.ArrayList;
import java.util.Collection;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.Resource;
import bitwise.devices.kinds.fullcamera.ExposureIndex;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbRequest;

public class PtpSetExposureIndexRequest extends UsbRequest {
	private final static ArrayList<Resource> emptyResource = new ArrayList<>(0);
	
	private final PtpCamera driver;
	private final ExposureIndex value;
	
	public PtpSetExposureIndexRequest(PtpCamera in_driver, ExposureIndex in_value) {
		super("PtpSetExposureIndexRequest");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
		assert(null != value);
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
		try {
			driver.cmd_setExposureIndex(value);
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
