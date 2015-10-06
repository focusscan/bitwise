package bitwise.devices.kinds.fullcamera.events;

import java.util.List;

import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FNumberChanged extends Event {
	private final FullCamera driver;
	private final FNumber value;
	private final List<FNumber> values;
	
	public FNumberChanged(FullCamera in_driver, FNumber in_value, List<FNumber> in_values) {
		super("F-Number Changed");
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
	
	public FNumber getFNumber() {
		return value;
	}
	
	public List<FNumber> getValidFNumbers() {
		return values;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera f-number to %s.", value);
	}
}
