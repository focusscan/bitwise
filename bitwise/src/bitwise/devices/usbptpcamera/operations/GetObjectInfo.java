package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.responses.ObjectInfo;

public class GetObjectInfo extends Operation<ObjectInfo> {
	public static final int objectID = 0;
	
	public GetObjectInfo(int in_objectID) {
		super((short) 0x1008, 1);
		this.setArgument(objectID, in_objectID);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private ObjectInfo decoded = null;
	
	@Override
	public ObjectInfo getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			decoded = new ObjectInfo(getResponseData().getData());
		}
		return decoded;
	}
}
