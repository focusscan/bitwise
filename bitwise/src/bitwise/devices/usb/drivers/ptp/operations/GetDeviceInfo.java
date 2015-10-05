package bitwise.devices.usb.drivers.ptp.operations;

import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDeviceInfo extends BaseOperation {
	public static final UInt16 operationCode = new UInt16((short) 0x1001);
	
	public GetDeviceInfo() {
		super("GetDeviceInfo", operationCode, TransactionID.zero, new ArrayList<>(0));
	}
}
