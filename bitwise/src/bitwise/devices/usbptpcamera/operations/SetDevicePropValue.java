package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;

public class SetDevicePropValue extends Operation<Void> {
	public static final int argDeviceProp = 0;
	
	public SetDevicePropValue(short in_deviceProp, UsbPtpPrimType value) {
		super((short) 0x1016, 1, value);
		this.setArgument(argDeviceProp, in_deviceProp);
	}

	@Override
	public boolean hasTransactionID() {
		return false;
	}
}
