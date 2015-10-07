package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureModeChanged extends Event {
	private final FullCamera driver;
	private final ExposureMode value;
	private final List<ExposureMode> values;
	
	public ExposureModeChanged(FullCamera in_driver, ExposureMode in_value, List<ExposureMode> in_values) {
		super("Exposure Mode Changed");
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
	
	public ExposureMode getExposureMode() {
		return value;
	}
	
	public List<ExposureMode> getValidExposureModes() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera exposure mode has changed to %s.", value);
	}
}
