package bitwise.devices.canon.operations;

import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.operations.Operation;

public class StopImageCapture extends Operation<Void> {
	public StopImageCapture(CanonDeviceProperties.ShutterState ss) {
		super((short) 0x9129, 1);
		setArgument(0, ss.getInt());
	}
	
	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
