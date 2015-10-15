package bitwise.devices.nikon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class EndLiveView extends Operation<Void> {
	public EndLiveView() {
		super((short) 0x9202, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
