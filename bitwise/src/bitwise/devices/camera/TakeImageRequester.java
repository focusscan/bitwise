package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface TakeImageRequester extends BaseRequester {
	public void notifyRequestComplete(TakeImageRequest in);
}
