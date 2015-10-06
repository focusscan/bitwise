package bitwise.devices.usb.drivers.ptp.types.containers;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.events.EventCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class EventContainer extends Container implements DecodableContainer {
	public static Decoder<EventContainer> decoder = new Decoder<EventContainer>() {
		@Override
		public EventContainer decode(ByteBuffer in) {
			return new EventContainer(in);
		}
	};
	
	private final EventCode eventCode;
	private final TransactionID transactionID;
	private final ByteBuffer payload;
	
	protected EventContainer(ByteBuffer in) {
		super(in);
		eventCode = EventCode.decoder.decode(in);
		transactionID = TransactionID.decoder.decode(in);
		payload = in.slice();
	}
	
	public EventCode getCode() {
		return eventCode;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	@Override
	public ByteBuffer getPayload() {
		return payload.slice();
	}
}
