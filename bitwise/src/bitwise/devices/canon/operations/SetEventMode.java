package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class SetEventMode extends Operation<Void> {
	private final int value = 1;
	
	public SetEventMode() {
		super((short) 0x9115, 1);
		setArgument(0, value);
	}
	
	@Override
	public boolean hasTransactionID() {
		return true;
	}
}