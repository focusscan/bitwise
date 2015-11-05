package bitwise.devices.usbptpcamera.operations;

public class DeleteObject extends Operation<Void> {
	public static final int argObjectID = 0;
	public static final int argFormat   = 1;
	
	public DeleteObject(int in_objectID) {
		super((short) 0x100b, 2);
		this.setArgument(argObjectID, in_objectID);
		this.setArgument(argObjectID, 0x00000000);
	}

	@Override
	public boolean hasTransactionID() {
		return false;
	}
}
