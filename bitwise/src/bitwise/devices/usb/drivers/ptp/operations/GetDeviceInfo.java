package bitwise.devices.usb.drivers.ptp.operations;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.Unused;

public class GetDeviceInfo extends BaseOperation<Unused, Unused, Unused, Unused, Unused> {
	public static final GetDeviceInfo instance = new GetDeviceInfo();
	
	private GetDeviceInfo() {
		super(new UInt16((short) 0x1001), null, Unused.instance, Unused.instance, Unused.instance, Unused.instance, Unused.instance);
	}
}
