package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface GetLiveViewImageRequest extends Request {
	public byte[] getImage();
}
