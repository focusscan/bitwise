package bitwise.devices.usbservice.requests;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.engine.service.RequestContext;
import bitwise.engine.service.requests.BaseRequest;

public final class StartUsbDriver<R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> extends BaseRequest<UsbService, StartUsbDriverRequester> implements UsbServiceRequest {
	private final UsbReady<R, H, A> ready;
	private H handle;
	
	public StartUsbDriver(UsbService in_service, StartUsbDriverRequester in_requester, UsbReady<R, H, A> in_ready) {
		super(in_service, in_requester);
		ready = in_ready;
	}
	
	public UsbReady<R, H, A> getReady() {
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
