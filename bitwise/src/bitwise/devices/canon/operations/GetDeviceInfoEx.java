package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class GetDeviceInfoEx extends Operation<Void> {
	
	public GetDeviceInfoEx() {
		super((short) 0x9108, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
