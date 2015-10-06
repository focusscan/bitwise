package bitwise.devices.kinds.fullcamera.events;

import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.engine.eventbus.Event;

public class ExposureModeChanged extends Event {
	private final FullCamera driver;
	private final ExposureMode exposureMode;
	
	public ExposureModeChanged(FullCamera in_driver, ExposureMode in_exposureMode) {
		super("Exposure Mode Changed");
		driver = in_driver;
		exposureMode = in_exposureMode;
		assert(null != driver);
		assert(null != exposureMode);
	}
	
	public FullCamera getDriver() {
		return driver;
	}
	
	public ExposureMode getExposureMode() {
		return exposureMode;
	}
	
	@Override
	public String getDescription() {
		return String.format("Camera exposure mode has changed to %s.", exposureMode);
	}
}
