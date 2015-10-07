package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.ExposureIndex;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureIndexChanged extends Event {
	private final FullCamera driver;
	private ExposureIndex value;
	private List<ExposureIndex> values;
	
	public ExposureIndexChanged(FullCamera in_driver, ExposureIndex in_value, List<ExposureIndex> in_values) {
		super("Exposure Index Changed");
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
	
	public ExposureIndex getExposureIndex() {
		return value;
	}
	
	public List<ExposureIndex> getValidExposureIndicies() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera exposure index now %s.", value);
	}
}
