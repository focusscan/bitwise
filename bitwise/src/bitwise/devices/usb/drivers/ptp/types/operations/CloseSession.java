package bitwise.devices.usb.drivers.ptp.types.operations;

public class CloseSession extends Operation<Void> {
	public CloseSession() {
		super("CloseSession", OperationCode.closeSession, 0, null);
	}
}