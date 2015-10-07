package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.kinds.fullcamera.StorageDevice;
import bitwise.engine.eventbus.Event;

public class StorageDevicesChanged extends Event {
	private final FullCamera driver;
	private final List<StorageDevice> values;
	
	public StorageDevicesChanged(FullCamera in_driver, List<StorageDevice> in_values) {
		super("Flash Mode Changed");
		driver = in_driver;
		values = in_values;
		assert(null != driver);
		assert(null != values);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public List<StorageDevice> getStorageDevices() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera storage devices changed.");
	}
}
