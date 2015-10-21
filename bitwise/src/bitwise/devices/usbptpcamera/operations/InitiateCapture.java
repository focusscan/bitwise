package bitwise.devices.usbptpcamera.operations;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.events.EventCode;
import bitwise.devices.usbptpcamera.responses.ResponseCode;

public class InitiateCapture extends Operation<Void> {
	public static final int argStorageID = 0;
	public static final int argObjectFormat = 1;
	
	private final ArrayList<Int32> objectIDs = new ArrayList<>(1);
	
	public InitiateCapture(int storageID, short objectFormat) {
		super((short) 0x100e, 2);
		this.setArgument(argStorageID, storageID);
		this.setArgument(argObjectFormat, objectFormat);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	public List<Int32> getObjectIDs() {
		return objectIDs;
	}
	
	@Override
	public void recvResponseCode(ResponseCode response) {
		responseCode = response;
		if (responseCode.getResponseCode() != ResponseCode.success)
			notifyFinished();
	}
	
	@Override
	public void recvInterruptData(Event event) {
		if (event.getEventCode() == EventCode.captureComplete)
			notifyFinished();
		else if (event.getEventCode() == EventCode.objectAdded)
			objectIDs.add(new Int32(event.getArguments()[0]));
	}
}
