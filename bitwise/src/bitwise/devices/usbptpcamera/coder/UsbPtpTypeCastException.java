package bitwise.devices.usbptpcamera.coder;

import bitwise.devices.usbptpcamera.UsbPtpException;

public class UsbPtpTypeCastException extends UsbPtpException {
	private static final long serialVersionUID = -3770907724215748790L;
	public final Class<? extends UsbPtpPrimType> actual;
	public final Class<? extends UsbPtpPrimType> requested;

	protected UsbPtpTypeCastException(Class<? extends UsbPtpPrimType> in_actual, Class<? extends UsbPtpPrimType> in_requested) {
		super(String.format("Casting exception: cannot cast %s to %s", in_actual.getSimpleName(), in_requested.getSimpleName()));
		actual = in_actual;
		requested = in_requested;
	}

}
