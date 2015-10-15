package bitwise.devices.camera;

import java.util.List;

import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.engine.service.Request;

public interface TakeImageRequest extends Request {
	public ImageFormat getImageFormat();
	public StorageDevice getStorageDevice();
	public List<Int32> getObjectIDs();
	public byte[] getImage();
}
