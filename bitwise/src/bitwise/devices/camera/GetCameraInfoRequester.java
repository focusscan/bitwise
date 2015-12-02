package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface GetCameraInfoRequester extends BaseRequester {
	public void notifyRequestComplete(GetCameraInfoRequest in);
}
