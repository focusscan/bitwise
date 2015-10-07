package bitwise.devices.usb.drivers.ptp;

import java.util.ArrayList;
import java.util.Collection;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.Resource;
import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbRequest;

public class PtpSetFocusModeRequest extends UsbRequest {
	private final static ArrayList<Resource> emptyResource = new ArrayList<>(0);
	
	private final PtpCamera driver;
	private final FocusMode value;
	
	public PtpSetFocusModeRequest(PtpCamera in_driver, FocusMode in_value) {
		super("PtpSetFocusModeRequest");
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
			driver.cmd_setFocusMode(value);
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}