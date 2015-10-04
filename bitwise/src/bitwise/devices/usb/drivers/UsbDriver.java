package bitwise.devices.usb.drivers;

import bitwise.apps.App;
import bitwise.apps.Resource;
import bitwise.devices.kinds.DeviceKind;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.events.UsbDriverClosedEvent;
import bitwise.devices.usb.events.UsbDriverInitializeFailedEvent;
import bitwise.devices.usb.events.UsbDriverInitializedEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class UsbDriver implements Resource, DeviceKind {
	private final UsbDriverID id;
	private final App app;
	private UsbDevice device = null;
	
	public UsbDriver(App in_app) {
		id = new UsbDriverID();
		app = in_app;
		assert(null != app);
	}
	
	@Override
	public final UsbDriverID getID() {
		return id;
	}
	
	protected final UsbDevice getDevice() {
		return device;
	}
	
	protected abstract boolean onDriverInitialize(UsbContext ctx) throws InterruptedException;
	protected abstract void onDriverDisable();
	public abstract String getName();
	
	private final BooleanProperty resourceIsOpen = new SimpleBooleanProperty(false);
	private boolean driverInitialized = false;
	
	public synchronized final void resourceClose() {
		if (resourceIsOpen.get()) {
			UsbDevice oldDevice = device;
			if (driverInitialized)
				onDriverDisable();
			resourceIsOpen.set(false);
			bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new UsbDriverClosedEvent(oldDevice, this));
			device.unsetDriver();
			device = null;
		}
	}
	
	public final BooleanProperty getResourceIsOpen() {
		return resourceIsOpen;
	}
	
	public synchronized final boolean initialize(UsbContext ctx, UsbDevice in_device) throws InterruptedException {
		device = in_device;
		resourceIsOpen.set(true);
		driverInitialized = onDriverInitialize(ctx);
		if (driverInitialized)
			bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new UsbDriverInitializedEvent(device, this));
		else {
			bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new UsbDriverInitializeFailedEvent(device, this));
			resourceClose();
		}
		return driverInitialized;
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
}
