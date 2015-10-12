package bitwise.devices.usbservice.requests;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.engine.service.RequestContext;
import bitwise.engine.service.requests.BaseRequest;

public final class AddDriverFactory<R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> extends BaseRequest<UsbService, AddDriverFactoryRequester> implements UsbServiceRequest {
	private final UsbDriverFactory<R, H, A> factory;
	
	public AddDriverFactory(UsbService in_service, AddDriverFactoryRequester in_requester, UsbDriverFactory<R, H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public UsbDriverFactory<R, H, A> getAppFactory() {
		return factory;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().addDriverFactory(factory);
	}
	
	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
