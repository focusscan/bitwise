package bitwise.devices.nikon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class DeviceReady extends Operation<Void> {
	public DeviceReady() {
		super((short) 0x90c8, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
