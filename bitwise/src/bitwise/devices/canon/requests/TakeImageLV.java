package bitwise.devices.canon.requests;

import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.devices.camera.TakeImageLVRequester;
import bitwise.devices.canon.BaseCanon;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class TakeImageLV extends BaseUsbPtpCameraRequest<BaseCanon, TakeImageLVRequester> implements TakeImageLVRequest {
	public TakeImageLV(BaseCanon in_service, TakeImageLVRequester in_requester) {
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
		getService().takeImageLV(this);
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
	
}