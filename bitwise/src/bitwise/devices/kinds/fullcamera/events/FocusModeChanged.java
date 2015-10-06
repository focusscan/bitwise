package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FocusModeChanged extends Event {
	private final FullCamera driver;
	private final FocusMode value;
	
	public FocusModeChanged(FullCamera in_driver, FocusMode in_value) {
		super("Focus Mode Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
		assert(null != value);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public FocusMode getFocusMode() {
		return value;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera focus mode has changed to %s.", value);
	}
}
