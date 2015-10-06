package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureIndexChanged extends Event {
	private final FullCamera driver;
	private short value;
	
	public ExposureIndexChanged(FullCamera in_driver, short in_value) {
		super("Exposure Index Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public short getExposureIndex() {
		return value;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera exposure index now %s.", value);
	}
}
