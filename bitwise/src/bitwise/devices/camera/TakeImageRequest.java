package bitwise.devices.camera;

import java.util.List;

import bitwise.devices.usbptpcamera.coder.Int32;

public interface TakeImageRequest {
	public ImageFormat getImageFormat();
	public StorageDevice getStorageDevice();
	public List<Int32> getObjectIDs();
	public byte[] getImage();
}
