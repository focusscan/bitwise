package bitwise.devices.usbptpcamera;

import bitwise.devices.BaseDriverHandle;

public abstract class BaseUsbPtpCameraHandle<A extends BaseUsbPtpCamera<?>> extends BaseDriverHandle<BaseUsbPtpCameraRequest<A, ?>, A> {
	protected BaseUsbPtpCameraHandle(A in_service) {
		super(in_service);
	}
}
