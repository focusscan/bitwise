package bitwise.engine.service;

import java.util.concurrent.BlockingQueue;

import bitwise.log.Log;

public abstract class BaseServiceHandle<R extends BaseRequest<?, ?>, S extends BaseService<?>> {
	private final ServiceHandleCertificate cert;
	private final S service;
	private final BlockingQueue<BaseRequest<?, ?>> incomingRequests;
	
	protected BaseServiceHandle(S in_service) {
		cert = new ServiceHandleCertificate();
		service = in_service;
		incomingRequests = service.getRequestHandler().getIncomingRequests(cert);
	}
	
	protected final S getService() {
		return service;
	}
	
	protected final void enqueueRequest(R in) {
		if (!(in instanceof BaseRequest<?, ?>))
			throw new IllegalArgumentException("Request");
		if (in.tryEnqueueServeRequest(cert)) {
			incomingRequests.add(in);
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
			incomingRequests.add(in);
			Log.log(service, "Enqueued for epilogue: %s", in);
		}
		else
			Log.log(service, "Failed to enqueue for epilogue: %s", in);
	}
	
	public final boolean serviceIsRunning() {
		return service.getRequestHandler().serviceIsRunning();
	}
}
