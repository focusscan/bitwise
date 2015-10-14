package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface GetPropertyRequester extends BaseRequester {
	public void notifyRequestComplete(GetPropertyRequest<?> in);
}
