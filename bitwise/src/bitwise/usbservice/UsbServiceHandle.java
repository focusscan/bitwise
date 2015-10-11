package bitwise.usbservice;

import bitwise.engine.service.ServiceHandle;

public final class UsbServiceHandle extends ServiceHandle<UsbRequest, UsbService> {
	protected UsbServiceHandle(UsbService in_service) {
		super(in_service);
	}
}
