package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class BatteryLevelChanged extends Event {
	private final FullCamera driver;
	private byte value;
	
	public BatteryLevelChanged(FullCamera in_driver, byte in_value) {
		super("Battery Level Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public byte getBatteryLevel() {
		return value;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera battery level now %s.", value);
	}
}
