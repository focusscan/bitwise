package bitwise.devices.usb.drivers.nikon;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;

import bitwise.apps.App;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.drivers.ptp.PTPCamera;

public abstract class NikonBase extends PTPCamera {
	private static final byte dataInEPNum = (byte)0x02;
	private static final byte dataOutEPNum = (byte)0x81;
	private static final byte interruptEPNum = (byte)0x83;
	
	public NikonBase(App in_app, UsbDevice in_device) {
		super(dataInEPNum, dataOutEPNum, interruptEPNum, in_app, in_device);
	}

	@Override
	public boolean initialize() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		if (!super.initialize())
			return false;
		// TODO
		return true;
	}
}
