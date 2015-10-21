package bitwise.devices.usbservice.requests;

import bitwise.devices.BaseDriverHandle;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.engine.service.RequestContext;

public final class AddUsbDriverFactory<H extends BaseDriverHandle<?, ?>> extends UsbServiceRequest<AddUsbDriverFactoryRequester> {
	private final UsbDriverFactory<H> factory;
	
	public AddUsbDriverFactory(UsbService in_service, AddUsbDriverFactoryRequester in_requester, UsbDriverFactory<H> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public UsbDriverFactory<H> getDriverFactory() {
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
