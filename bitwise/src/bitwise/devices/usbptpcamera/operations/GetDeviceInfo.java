package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class GetDeviceInfo extends Operation<DeviceInfo> {
	public GetDeviceInfo() {
		super((short) 0x1001, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private DeviceInfo decoded = null;
	
	@Override
	public DeviceInfo getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			decoded = new DeviceInfo(getResponseData().getData());
		}
		return decoded;
	}
}
