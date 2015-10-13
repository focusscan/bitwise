package bitwise.devices.usbptpcamera.operations;

public class OpenSession extends Operation<Void> {
	public static final int argSessionID = 0;
	
	public OpenSession(int in_sessionID) {
		super((short) 0x1002, 1);
		this.setArgument(argSessionID, in_sessionID);
	}

	@Override
	public boolean hasTransactionID() {
		return false;
	}
}
