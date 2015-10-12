package bitwise.devices.nikon;

import bitwise.devices.nikon.NikonCameraRequest;
import bitwise.devices.usbptpcamera.UsbPtpCamera;
import bitwise.devices.usbservice.UsbDevice;

public class NikonBase extends UsbPtpCamera<NikonCameraRequest, NikonHandle> {
	private final NikonHandle handle;
	
	protected NikonBase(UsbDevice in_device) {
		super(in_device);
		handle = new NikonHandle(this);
	}

	@Override
	protected boolean onStartPtpDriver() {
		return true;
	}

	@Override
	protected void onStopPtpDriver() {
	}

	@Override
	public NikonHandle getServiceHandle() {
		return handle;
	}
}
