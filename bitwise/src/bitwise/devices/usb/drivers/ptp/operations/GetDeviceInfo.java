package bitwise.devices.usb.drivers.ptp.operations;

import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDeviceInfo extends BaseOperation {
	public static final GetDeviceInfo instance = new GetDeviceInfo();
	
	private GetDeviceInfo() {
		super(new UInt16((short) 0x1001), null, new ArrayList<>(0));
	}
}
