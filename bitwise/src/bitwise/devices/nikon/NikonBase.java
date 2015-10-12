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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onStopPtpDriver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NikonHandle getServiceHandle() {
		return handle;
	}
}
