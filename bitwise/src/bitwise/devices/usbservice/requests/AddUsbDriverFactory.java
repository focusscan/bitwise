package bitwise.devices.usbservice.requests;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.engine.service.RequestContext;

public final class AddUsbDriverFactory<H extends BaseDriverHandle<?, ?>, A extends BaseDriver<UsbDevice, H>> extends UsbServiceRequest<AddUsbDriverFactoryRequester> {
	private final UsbDriverFactory<H, A> factory;
	
	public AddUsbDriverFactory(UsbService in_service, AddUsbDriverFactoryRequester in_requester, UsbDriverFactory<H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public UsbDriverFactory<H, A> getDriverFactory() {
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
