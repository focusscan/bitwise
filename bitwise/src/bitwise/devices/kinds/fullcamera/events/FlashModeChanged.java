package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FlashModeChanged extends Event {
	private final FullCamera driver;
	private final FlashMode value;
	private final List<FlashMode> values;
	
	public FlashModeChanged(FullCamera in_driver, FlashMode in_value, List<FlashMode> in_values) {
		super("Flash Mode Changed");
		driver = in_driver;
		value = in_value;
		values = in_values;
		assert(null != driver);
		assert(null != value);
		assert(null != values);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public FlashMode getFlashMode() {
		return value;
	}
	
	public List<FlashMode> getValidFlashModes() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera flash mode has changed to %s.", value);
	}
}
