package bitwise.devices.nikon.requests;

import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.GetLiveViewImageRequester;
import bitwise.devices.nikon.BaseNikon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class GetLiveViewImage extends BaseUsbPtpCameraRequest<BaseNikon, GetLiveViewImageRequester> implements GetLiveViewImageRequest {
	public GetLiveViewImage(BaseNikon in_service, GetLiveViewImageRequester in_requester) {
		super(in_service, in_requester);
	}
	
	private byte[] image = null;
	
	public void setImage(byte[] in) {
		image = in;
	}
	
	@Override
	public byte[] getImage() {
		return image;
	}
	
	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().getLiveViewImage(this);
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
