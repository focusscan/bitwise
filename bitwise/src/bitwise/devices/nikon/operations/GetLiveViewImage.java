package bitwise.devices.nikon.operations;

import bitwise.devices.nikon.reponses.LiveViewObject;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.operations.Operation;

public class GetLiveViewImage extends Operation<LiveViewObject> {
	public GetLiveViewImage() {
		super((short) 0x9203, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private LiveViewObject decoded = null;
	
	@Override
	public LiveViewObject getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			decoded = new LiveViewObject(getResponseData().getData());
		}
		return decoded;
	}
}
