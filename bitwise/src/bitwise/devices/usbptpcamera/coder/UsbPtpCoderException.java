package bitwise.devices.usbptpcamera.coder;

import bitwise.devices.usbptpcamera.UsbPtpException;

public class UsbPtpCoderException extends UsbPtpException {
	private static final long serialVersionUID = -2681369625033867921L;
	public final Exception exception;
	
	protected UsbPtpCoderException(Exception in) {
		super(String.format("UsbPtpCoderException - %s", in.getMessage()));
		exception = in;
	}
}
