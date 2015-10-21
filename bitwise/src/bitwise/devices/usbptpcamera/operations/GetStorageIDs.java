package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.coder.AInt32;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class GetStorageIDs extends Operation<AInt32> {
	public GetStorageIDs() {
		super((short) 0x1004, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private AInt32 decoded = null;
	
	@Override
	public AInt32 getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			decoded = new AInt32(getResponseData().getData().getIntArray());
		}
		return decoded;
	}
}
