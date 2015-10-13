package bitwise.devices.usbptpcamera.operations;

public class GetDeviceInfo extends Operation {
	public GetDeviceInfo() {
		super((short) 0x1001, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
