package bitwise.devices.usbptpcamera.operations;

public class CloseSession extends Operation {
	public CloseSession() {
		super((short) 0x1003, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
