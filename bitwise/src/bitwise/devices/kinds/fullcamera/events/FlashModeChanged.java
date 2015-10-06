package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FlashModeChanged extends Event {
	private final FullCamera driver;
	private final FlashMode value;
	
	public FlashModeChanged(FullCamera in_driver, FlashMode in_value) {
		super("Flash Mode Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
		assert(null != value);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public FlashMode getFocusMode() {
		return value;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera flash mode has changed to %s.", value);
	}
}
