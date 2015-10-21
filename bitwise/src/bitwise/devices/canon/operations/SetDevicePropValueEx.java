package bitwise.devices.canon.operations;

import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.coder.ByteLenAInt32;
import bitwise.devices.usbptpcamera.coder.Int16;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.coder.UsbPtpTypeCastException;
import bitwise.devices.usbptpcamera.operations.Operation;

public class SetDevicePropValueEx extends Operation<Void> {
	
	public SetDevicePropValueEx(short p, UsbPtpPrimType v) {
		super((short) 0x9110, 0);
		int val = 0;
		
		try {
			if (v instanceof Int16)
				val = 0xffff & v.castTo(Int16.class).value;
			else if (v instanceof Int32)
				val = v.castTo(Int32.class).value;
		} catch (UsbPtpTypeCastException e) {
			System.out.println("ERROR: Shouldn't happen!");
			e.printStackTrace();
		}
		
		ByteLenAInt32 data = new ByteLenAInt32(new int[] {CanonDeviceProperties.toCanonPropCode(p), val});
		dataOut = data;
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
}
