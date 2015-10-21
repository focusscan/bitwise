package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface LiveViewOnRequester extends BaseRequester {
	public void notifyRequestComplete(LiveViewOnRequest in);
}
