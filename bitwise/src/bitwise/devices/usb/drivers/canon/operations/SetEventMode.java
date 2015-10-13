package bitwise.devices.usb.drivers.canon.operations;

import bitwise.devices.usb.drivers.ptp.types.operations.Operation;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class SetEventMode extends Operation<Void> {
	private final UInt32 value = new UInt32(1);
	
	public SetEventMode() {
		super("SetCanonEventMode", CanonOperationCode.setEventMode, 0, null);
		getArguments().add(value);
	}
}
