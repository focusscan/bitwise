package bitwise.devices.usb;

import bitwise.apps.App;
import bitwise.devices.kinds.DeviceKind;

public abstract class UsbDriver implements DeviceKind {
	private final UsbDriverID id;
	private final App app;
	private final UsbDevice device;
	
	public UsbDriver(App in_app, UsbDevice in_device) {
		id = new UsbDriverID();
		app = in_app;
		device = in_device;
		assert(null != app);
		assert(null != device);
	}
	
	@Override
	public final UsbDriverID getDriverID() {
		return id;
	}
	
	@Override
	public final UsbDevice getDevice() {
		return device;
	}
	
	protected final javax.usb.UsbDevice getPlatformDevice() {
		return device.getPlatformDevice();
	}
	
	public final void disableDriver() {
		device.unsetDriver();
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
	
	public abstract String getName();
}
