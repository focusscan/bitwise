package bitwise.devices.usbptpcamera;

import bitwise.devices.Driver;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.engine.service.Request;

public abstract class UsbPtpCamera<R extends UsbPtpCameraRequest, H extends UsbPtpCameraHandle<R, ?>> extends Driver<UsbDevice, R, H> {
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
	protected void onRequestComplete(Request in) {
		// TODO Auto-generated method stub
		
	}
}
