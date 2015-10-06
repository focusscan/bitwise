package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class FocalLengthChanged extends Event {
	private final FullCamera driver;
	private int value;
	
	public FocalLengthChanged(FullCamera in_driver, int in_value) {
		super("Focal Length Changed");
		driver = in_driver;
		value = in_value;
		assert(null != driver);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public int getFocalLength() {
		return value;
	}
	
	@Override
	public String getDescription() {
		int whole = value / 100;
		int part  = value % 100;
		return String.format("Camera focal length is %s.%s mm.", whole, part);
	}
}
