package bitwise.devices.nikon.operations;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.operations.Operation;

public class GetEvent extends Operation<List<Event>> {
	public GetEvent() {
		super((short) 0x90c7, 0);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private List<Event> decoded = null;
	
	@Override
	public List<Event> getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			UsbPtpBuffer buf = this.getResponseData().getData();
			final short eventCount = buf.getShort();
			decoded = new ArrayList<Event>(eventCount);
			for (int i = 0; i < eventCount; i++) {
				short eventCode = buf.getShort();
				int[] eventArg = new int[1];
				eventArg[0] = buf.getInt();
				decoded.add(new Event(eventCode, eventArg));
			}
		}
		return decoded;
	}
}
