package bitwise.devices.canon.operations;

import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.operations.Operation;

public class StartImageCapture extends Operation<Void> {	
	public StartImageCapture(CanonDeviceProperties.ShutterState ss) {
		super((short) 0x9128, 2);
		setArgument(0, ss.getInt());
		setArgument(1, 0x0);
	}
	
	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
