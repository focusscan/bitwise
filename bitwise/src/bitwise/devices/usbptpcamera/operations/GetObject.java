package bitwise.devices.usbptpcamera.operations;

public class GetObject extends Operation<Void> {
	public static final int argObjectID = 0;
	
	public GetObject(int objectID) {
		super((short) 0x1009, 1);
		this.setArgument(argObjectID, objectID);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
