package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface DriveFocusRequester extends BaseRequester {
	public void notifyRequestComplete(DriveFocusRequest in);
}
