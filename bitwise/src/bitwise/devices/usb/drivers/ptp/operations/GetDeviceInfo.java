package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.responses.DeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDeviceInfo extends BaseOperation<DeviceInfo> {
	public static final UInt16 operationCode = new UInt16((short) 0x1001);
	
	public GetDeviceInfo(TransactionID in_transactionID) {
		super("GetDeviceInfo", operationCode, in_transactionID, 0);
	}

	@Override
	public DeviceInfo decodeResponse(ByteBuffer in) {
		return new DeviceInfo(in);
	}
}
