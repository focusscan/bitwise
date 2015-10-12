package bitwise.devices.usbservice;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.devices.usbservice.requests.AddUsbDriverFactory;
import bitwise.devices.usbservice.requests.AddUsbDriverFactoryRequester;
import bitwise.engine.service.ServiceHandle;

public final class UsbServiceHandle extends ServiceHandle<UsbServiceRequest, UsbService> {
	protected UsbServiceHandle(UsbService in_service) {
		super(in_service);
	}
	
	public <R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> AddUsbDriverFactory<R, H, A> addUsbDriverFactory(AddUsbDriverFactoryRequester requester, UsbDriverFactory<R, H, A> factory) {
		return new AddUsbDriverFactory<>(getService(), requester, factory);
	}
}
