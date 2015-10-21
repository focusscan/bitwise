package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface LiveViewOffRequester extends BaseRequester {
	public void notifyRequestComplete(LiveViewOffRequest in);
}
