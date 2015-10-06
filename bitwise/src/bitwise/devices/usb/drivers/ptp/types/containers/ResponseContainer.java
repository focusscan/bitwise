package bitwise.devices.usb.drivers.ptp.types.containers;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.responses.ResponseCode;

public class ResponseContainer extends Container implements DecodableContainer {
	public static Decoder<ResponseContainer> decoder = new Decoder<ResponseContainer>() {
		@Override
		public ResponseContainer decode(ByteBuffer in) {
			return new ResponseContainer(in);
		}
	};
	
	private final ResponseCode responseCode;
	private final TransactionID transactionID;
	private final ByteBuffer payload;
	
	protected ResponseContainer(ByteBuffer in) {
		super(in);
		responseCode = ResponseCode.decoder.decode(in);
		transactionID = TransactionID.decoder.decode(in);
		payload = in.slice();
	}
	
	public ResponseCode getCode() {
		return responseCode;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	public ByteBuffer getPayload() {
		return payload.slice();
	}
}
