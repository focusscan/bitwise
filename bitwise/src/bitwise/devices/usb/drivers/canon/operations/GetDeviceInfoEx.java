package bitwise.devices.usb.drivers.canon.operations;

import bitwise.devices.usb.drivers.ptp.types.operations.Operation;


public class GetDeviceInfoEx extends Operation<Void> {
	
	public GetDeviceInfoEx() {
		super("GetCanonDeviceInfoEx", CanonOperationCode.getDeviceInfoEx, 0, null);
	}
}
