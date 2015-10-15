package bitwise.devices.nikon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class StartLiveView extends Operation<Void> {
	public StartLiveView() {
		super((short) 0x9201, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
