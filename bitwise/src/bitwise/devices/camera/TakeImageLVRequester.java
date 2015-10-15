package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface TakeImageLVRequester extends BaseRequester {
	public void notifyRequestComplete(TakeImageLVRequest in);
}
