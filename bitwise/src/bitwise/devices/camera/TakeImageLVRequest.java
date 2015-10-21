package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface TakeImageLVRequest extends Request {
	public byte[] getImage();
}
