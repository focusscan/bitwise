package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class SetRemoteMode extends Operation<Void> {
	private final int value = 1;
	
	public SetRemoteMode() {
		super((short) 0x9114, 1);
		setArgument(0, value);
	}
	
	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
