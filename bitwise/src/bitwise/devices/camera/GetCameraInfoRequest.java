package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface GetCameraInfoRequest extends Request {
	public String getCameraManufacturer();
	public String getCameraModel();
	public String getCameraVersion();
	public String getCameraSerial();
}
