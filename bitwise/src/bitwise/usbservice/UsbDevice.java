package bitwise.usbservice;

import bitwise.engine.Thing;

public class UsbDevice extends Thing<UsbDeviceID> {
	protected UsbDevice(UsbDeviceID in_id) {
		super(new UsbDeviceID());
	}
}
