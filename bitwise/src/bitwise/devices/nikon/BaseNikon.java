package bitwise.devices.nikon;

import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbservice.UsbDevice;

public abstract class BaseNikon extends BaseUsbPtpCamera<NikonHandle> {
	private static final byte interfaceNum = (byte)0x00;
	private static final byte dataInNum = (byte)0x02;
	private static final byte dataOutNum = (byte)0x81;
	private static final byte intrptNum = (byte)0x83;
	
	private final NikonHandle handle;
	
	protected BaseNikon(UsbDevice in_device) {
		super(in_device, interfaceNum, dataInNum, dataOutNum, intrptNum);
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
