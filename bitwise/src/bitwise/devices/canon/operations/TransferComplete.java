package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class TransferComplete extends Operation<Void> {
	public TransferComplete(int objectID) {
		super((short) 0x9117, 1);
		setArgument(0, objectID);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
