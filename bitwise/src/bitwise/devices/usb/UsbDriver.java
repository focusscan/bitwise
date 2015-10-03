package bitwise.devices.usb;

import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;

import bitwise.apps.App;
import bitwise.devices.kinds.DeviceKind;

public abstract class UsbDriver implements DeviceKind, UsbDeviceListener {
	private final UsbDriverID id;
	private final App app;
	private final UsbDevice device;
	private javax.usb.UsbConfiguration cfg = null;
	private javax.usb.UsbInterface iface = null;
	
	public UsbDriver(App in_app, UsbDevice in_device) {
		id = new UsbDriverID();
		app = in_app;
		device = in_device;
		assert(null != app);
		assert(null != device);
		getPlatformDevice().addUsbDeviceListener(this);
	}
	
	@Override
	public final UsbDriverID getDriverID() {
		return id;
	}
	
	@Override
	public final UsbDevice getDevice() {
		return device;
	}
	
	protected final javax.usb.UsbInterface getIface() {
		return iface;
	}
	
	protected final javax.usb.UsbDevice getPlatformDevice() {
		return device.getPlatformDevice();
	}
	
	public boolean initialize() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		try {
			cfg = getPlatformDevice().getActiveUsbConfiguration();
			iface = cfg.getUsbInterface((byte) 0x00);
			iface.claim();
			return true;
		}
		catch (UsbClaimException e) {
			return false;
		}
	}
	
	public final void disableDriver() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		getPlatformDevice().removeUsbDeviceListener(this);
		device.unsetDriver();
		try {
			iface.release();
		} catch (UsbClaimException e) {
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("UsbDriver<%08x> (%s)", id.getValue(), getName());
	}
	
	@Override
	public synchronized final void dataEventOccurred(UsbDeviceDataEvent event) {
		System.out.println(String.format("dataEventOccurred(Device: %s)", event));
		byte data[] = event.getData();
		if (null != data) {
			System.out.print(String.format("Read %d bytes: ", data.length));
			for (byte b : data)
				System.out.print(String.format("%02x", b));
			System.out.println("");
		}
	}

	@Override
	public synchronized final void errorEventOccurred(UsbDeviceErrorEvent event) {
		System.out.println(String.format("errorEventOccurred(Device: %s)", event));
	}

	@Override
	public synchronized final void usbDeviceDetached(UsbDeviceEvent event) {
		try {
			disableDriver();
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
		}
	}
	
	public abstract String getName();
}
