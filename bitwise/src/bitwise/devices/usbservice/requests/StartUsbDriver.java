package bitwise.devices.usbservice.requests;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.engine.service.RequestContext;

public final class StartUsbDriver<H extends BaseDriverHandle<?, ?>, A extends BaseDriver<UsbDevice, H>> extends UsbServiceRequest<StartUsbDriverRequester> {
	private final UsbReady<H, A> ready;
	private H handle;
	
	public StartUsbDriver(UsbService in_service, StartUsbDriverRequester in_requester, UsbReady<H, A> in_ready) {
		super(in_service, in_requester);
		ready = in_ready;
	}
	
	public UsbReady<H, A> getReady() {
		return ready;
	}
	
	public H getHandle() {
		return handle;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		handle = getService().startDriver(getRequester(), ready.getDevice(), ready.getFactory());
	}
	
	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
