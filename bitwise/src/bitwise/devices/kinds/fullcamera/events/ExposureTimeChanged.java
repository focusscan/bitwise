package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureTimeChanged extends Event {
	private final FullCamera driver;
	private int value;
	
	public ExposureTimeChanged(FullCamera in_driver, int in_value) {
		super("Exposure Time Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public int getExposureTime() {
		return value;
	}
	
	@Override
	public String getDescription() {
		int whole = value / 10000;
		int part  = value % 10000;
		return String.format("Camera exposure time now %s.%s seconds.", whole, part);
	}
}
