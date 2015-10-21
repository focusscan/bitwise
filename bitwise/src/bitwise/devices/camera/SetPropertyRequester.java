package bitwise.devices.camera;

import bitwise.engine.service.BaseRequester;

public interface SetPropertyRequester extends BaseRequester {
	public void notifyRequestComplete(SetPropertyRequest<?> in);
}
