package bitwise.devices.usbptpcamera.operations;

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
	public DeviceInfo getDecodedData() {
		if (null == decoded && null != getResponseData()) {
			decoded = new DeviceInfo(getResponseData().getData());
		}
		return decoded;
	}
}
