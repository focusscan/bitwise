package bitwise.devices.canon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class UpdateStorageSpace extends Operation<Void> {
	
	public UpdateStorageSpace() {
		super((short) 0x911a, 3);
		
		/* Tell the camera we have enough space to download 
		 * TODO maybe not hardcode this?  If we want to store on the camera? */
		setArgument(0, 0x04ffffff);
		setArgument(1, 0x00001000);
		setArgument(2, 0x00000001);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
