package bitwise.devices.usbptpcamera;

import bitwise.devices.BaseDriver;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.engine.service.BaseRequest;

public abstract class UsbPtpCamera<H extends UsbPtpCameraHandle<? extends UsbPtpCameraRequest<?, ?>, ?>> extends BaseDriver<UsbDevice, H> {
	protected UsbPtpCamera(UsbDevice in_device) {
		super(in_device);
	}
	
	protected abstract boolean onStartPtpDriver();

	@Override
	protected boolean onStartDriver() {
		return onStartPtpDriver();
	}
	
	protected abstract void onStopPtpDriver();

	@Override
	protected void onStopDriver() {
		onStopPtpDriver();
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
	}
}
