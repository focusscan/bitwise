package bitwise.devices.nikon.operations;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.events.EventCode;
import bitwise.devices.usbptpcamera.operations.Operation;
import bitwise.devices.usbptpcamera.responses.ResponseCode;

public class InitiateCaptureLV extends Operation<Void> {
	public static final int argCaptureSort = 0;
	public static final int argMedia = 1;
	
	private final ArrayList<Int32> objectIDs = new ArrayList<>(1);
	
	public InitiateCaptureLV() {
		super((short) 0x9207, 2);
		this.setArgument(argCaptureSort, 0xffffffff);
		this.setArgument(argMedia, 0x0000);
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
