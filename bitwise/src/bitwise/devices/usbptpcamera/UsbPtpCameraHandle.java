package bitwise.devices.usbptpcamera;

import bitwise.devices.DriverHandle;

public abstract class UsbPtpCameraHandle<R extends UsbPtpCameraRequest, A extends UsbPtpCamera<R, ?>> extends DriverHandle<R, A> {
	protected UsbPtpCameraHandle(A in_service) {
		super(in_service);
	}
}
