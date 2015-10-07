package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FocusModeChanged extends Event {
	private final FullCamera driver;
	private final FocusMode value;
	private final List<FocusMode> values;
	
	public FocusModeChanged(FullCamera in_driver, FocusMode in_value, List<FocusMode> in_values) {
		super("Focus Mode Changed");
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
	
	public FocusMode getFocusMode() {
		return value;
	}
	
	public List<FocusMode> getValidFocusModes() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera focus mode has changed to %s.", value);
	}
}
