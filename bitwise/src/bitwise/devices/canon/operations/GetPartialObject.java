package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class GetPartialObject extends Operation<Void> {
	public GetPartialObject(int objectID, int objectSize) {
		super((short) 0x9107, 3);
		setArgument(0, objectID);
		setArgument(1, 0x0);
		setArgument(2, objectSize);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
