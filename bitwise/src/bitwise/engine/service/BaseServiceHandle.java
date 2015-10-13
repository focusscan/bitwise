package bitwise.engine.service;

import bitwise.log.Log;

public abstract class BaseServiceHandle<R extends BaseRequest<?, ?>, S extends BaseService<?>> {
	private final ServiceHandleCertificate cert = new ServiceHandleCertificate();
	private final S service;
	
	protected BaseServiceHandle(S in_service) {
		service = in_service;
	}
	
	protected final S getService() {
		return service;
	}
	
	public final void enqueueRequest(R in) {
		if (!(in instanceof BaseRequest<?, ?>))
			throw new IllegalArgumentException("Request");
		if (in.tryEnqueueServeRequest(cert)) {
			service.getRequestHandler().getIncomingRequests().add(in);
			in.getService().generalNotifyRequestEnqueued(in);
			Log.log(service, "Enqueued for serving: %s", in);
		}
		else
			Log.log(service, "Failed to enqueue for serving: %s", in);
	}
	
	protected final void enqueueEpilogue(ServiceCertificate serviceCert, BaseRequest<?, ?> in) {
		if (null == serviceCert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (in.tryEnqueueEpilogueRequest(cert)) {
			service.getRequestHandler().getIncomingRequests().add(in);
			Log.log(service, "Enqueued for epilogue: %s", in);
		}
		else
			Log.log(service, "Failed to enqueue for epilogue: %s", in);
	}
	
	public final boolean serviceIsRunning() {
		return service.getRequestHandler().serviceIsRunning();
	}
}
