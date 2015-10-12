package bitwise.devices.usbservice;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.devices.usbservice.requests.AddDriverFactory;
import bitwise.devices.usbservice.requests.AddDriverFactoryRequester;
import bitwise.engine.service.ServiceHandle;

public final class UsbServiceHandle extends ServiceHandle<UsbServiceRequest, UsbService> {
	protected UsbServiceHandle(UsbService in_service) {
		super(in_service);
	}
	
	public <R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> AddDriverFactory<R, H, A> addAppFactory(AddDriverFactoryRequester requester, UsbDriverFactory<R, H, A> factory) {
		return new AddDriverFactory<>(getService(), requester, factory);
	}
}
