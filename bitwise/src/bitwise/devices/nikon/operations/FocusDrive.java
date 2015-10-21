package bitwise.devices.nikon.operations;

import bitwise.devices.usbptpcamera.operations.Operation;

public class FocusDrive extends Operation<Void> {
	public static final int argDirection = 0;
	public static final int argSteps = 1;
	
	public FocusDrive(int direction, int steps) {
		super((short) 0x9204, 2);
		this.setArgument(argDirection, direction);
		this.setArgument(argSteps, steps);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
