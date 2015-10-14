package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface SetPropertyRequest<T> extends Request {
	public CameraProperty getProperty();
	public T getValue();
}
