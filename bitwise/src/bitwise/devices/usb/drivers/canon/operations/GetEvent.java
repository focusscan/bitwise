package bitwise.devices.usb.drivers.canon.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.canon.responses.CanonEventResponse;
import bitwise.devices.usb.drivers.ptp.types.operations.Operation;

public class GetEvent extends Operation<CanonEventResponse> {
	
	public GetEvent() {
		super("GetCanonEvent", CanonOperationCode.getEvent, 0, null);
	}
	
	private CanonEventResponse data;
	
	@Override
	public CanonEventResponse getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = CanonEventResponse.decoder.decode(in);
		return true;
	}
}
