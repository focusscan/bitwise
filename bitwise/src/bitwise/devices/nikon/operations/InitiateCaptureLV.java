package bitwise.devices.nikon.operations;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.operations.Operation;

public class InitiateCaptureLV extends Operation<Void> {
	public static final int argCaptureSort = 0;
	public static final int argMedia = 1;
	
	private final ArrayList<Int32> objectIDs = new ArrayList<>(1);
	
	public InitiateCaptureLV() {
		super((short) 0x9207, 2);
		this.setArgument(argCaptureSort, 0xffffffff);	// No AF drive
		this.setArgument(argMedia, 0x0001);				// Capture to sdram
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	public List<Int32> getObjectIDs() {
		return objectIDs;
	}
}
