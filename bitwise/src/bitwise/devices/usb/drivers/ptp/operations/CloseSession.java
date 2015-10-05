package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class CloseSession extends BaseOperation<BaseResponse> {
	public static final UInt16 operationCode = new UInt16((short) 0x1003);
	
	public CloseSession(TransactionID id) {
		super("CloseSession", operationCode, id, new ArrayList<>(0));
	}

	@Override
	public BaseResponse decodeResponse(ByteBuffer in) {
		return new BaseResponse(in);
	}
}
