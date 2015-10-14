package bitwise.devices.camera;

import java.util.List;

import bitwise.engine.service.Request;

public interface GetPropertyRequest<T> extends Request {
	public CameraProperty getProperty();
	public boolean gotValues();
	public T getValue();
	public List<T> getLegalValues();
	public boolean canSet();
}
