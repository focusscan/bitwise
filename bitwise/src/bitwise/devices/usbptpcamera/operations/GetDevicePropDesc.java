package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.responses.DevicePropDesc;

public class GetDevicePropDesc extends Operation<DevicePropDesc> {
	public static final int argDeviceProp = 0;
	
	public GetDevicePropDesc(short in_deviceProp) {
		super((short) 0x1014, 1);
		this.setArgument(argDeviceProp, in_deviceProp);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private DevicePropDesc decoded = null;
	
	@Override
	public DevicePropDesc getDecodedData() {
		if (null == decoded && null != getResponseData()) {
			decoded = new DevicePropDesc(getResponseData().getData());
		}
		return decoded;
	}
}
