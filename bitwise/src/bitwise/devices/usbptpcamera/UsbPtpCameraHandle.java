package bitwise.devices.usbptpcamera;

import bitwise.devices.BaseDriverHandle;

public abstract class UsbPtpCameraHandle<R extends UsbPtpCameraRequest<?, ?>, A extends UsbPtpCamera<?>> extends BaseDriverHandle<R, A> {
	protected UsbPtpCameraHandle(A in_service) {
		super(in_service);
	}
}
