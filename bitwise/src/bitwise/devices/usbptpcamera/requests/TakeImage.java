package bitwise.devices.usbptpcamera.requests;

import java.util.List;

import bitwise.devices.camera.ImageFormat;
import bitwise.devices.camera.StorageDevice;
import bitwise.devices.camera.TakeImageRequest;
import bitwise.devices.camera.TakeImageRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.engine.service.RequestContext;

public class TakeImage<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, TakeImageRequester> implements TakeImageRequest {
	private final ImageFormat imageFormat;
	private final StorageDevice storageDevice;
	private List<Int32> objectIDs = null;
	private byte[] image = null;
	
	public TakeImage(A in_service, TakeImageRequester in_requester, ImageFormat in_imageFormat, StorageDevice in_storageDevice) {
		super(in_service, in_requester);
		imageFormat = in_imageFormat;
		storageDevice = in_storageDevice;
	}
	
	@Override
	public ImageFormat getImageFormat() {
		return imageFormat;
	}
	
	@Override
	public StorageDevice getStorageDevice() {
		return storageDevice;
	}
	
	public void setObjectID(List<Int32> in) {
		objectIDs = in;
	}
	
	@Override
	public List<Int32> getObjectIDs() {
		return objectIDs;
	}
	
	public void setImage(byte[] in) {
		image = in;
	}
	
	@Override
	public byte[] getImage() {
		return image;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().takeImage(this);
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
