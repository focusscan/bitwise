package bitwise.engine.service;

import bitwise.engine.service.requests.BaseRequest;

public abstract class ServiceHandle<R extends Request, S extends Service<R, ?>> {
	private final S service;
	
	protected ServiceHandle(S in_service) {
		service = in_service;
	}
	
	protected final S getService() {
		return service;
	}
	
	public final void enqueueRequest(R in) {
		if (!(in instanceof BaseRequest<?, ?>))
			throw new IllegalArgumentException("Request");
		service.getRequestHandler().enqueueRequest(in);
		((BaseRequest<?, ?>)in).getService().generalNotifyRequestEnqueued(in);
	}
	
	public final boolean serviceIsRunning() {
		return service.getRequestHandler().serviceIsRunning();
	}
}
