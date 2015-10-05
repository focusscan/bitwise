package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.types.SessionID;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class OpenSession extends BaseOperation<BaseResponse> {
	public static final UInt16 operationCode = new UInt16((short) 0x1002);
	
	private final SessionID sessionID;
	
	public OpenSession() {
		super("OpenSession", operationCode, TransactionID.zero, new ArrayList<>(1));
		sessionID = new SessionID();
		getArgs().add(sessionID);
	}

	@Override
	public BaseResponse decodeResponse(ByteBuffer in) {
		return new BaseResponse(in);
	}
}
