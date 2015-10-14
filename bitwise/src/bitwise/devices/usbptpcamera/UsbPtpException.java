package bitwise.devices.usbptpcamera;

public abstract class UsbPtpException extends Exception {
	private static final long serialVersionUID = -598879676259216836L;

	protected UsbPtpException(String message) {
		super(message);
	}
}
