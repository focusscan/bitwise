package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface GetLiveViewImageRequester extends BaseRequester {
	public void notifyRequestComplete(GetLiveViewImageRequest in);
}
