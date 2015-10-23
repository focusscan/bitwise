package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class SetFocusDrive extends Operation<Void> {
	public SetFocusDrive(int focus) {
		super((short) 0x9155, 1);
		setArgument(0, focus);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
