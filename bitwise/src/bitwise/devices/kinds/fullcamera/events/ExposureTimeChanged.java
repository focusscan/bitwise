package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.ExposureTime;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureTimeChanged extends Event {
	private final FullCamera driver;
	private ExposureTime value;
	private List<ExposureTime> values;
	
	public ExposureTimeChanged(FullCamera in_driver, ExposureTime in_value, List<ExposureTime> in_values) {
		super("Exposure Time Changed");
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
	
	public ExposureTime getExposureTime() {
		return value;
	}
	
	public List<ExposureTime> getValidExposureTimes() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera exposure time now %s.", value);
	}
}
