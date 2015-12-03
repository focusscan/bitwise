package bitwise.engine.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bitwise.engine.Thing;
import bitwise.log.Log;

public abstract class BaseRequest<S extends BaseService<?>, R extends BaseRequester> extends Thing<RequestID> implements Request {
	private final S service;
	private final R requester;
	private final RequestState requestState = new RequestState();
	private CountDownLatch epiloguedLatch = new CountDownLatch(1);
	
	protected BaseRequest(S in_service, R in_requester) {
		super(new RequestID());
		service = in_service;
		requester = in_requester;
	}
	
	public final S getService() {
		return service;
	}
	
	public final R getRequester() {
		return requester;
	}
	
	public final RequestState getRequestState() {
		return requestState;
	}
	
	@Override
	public final boolean isCancelled() {
		return requestState.getRequestStatus() == RequestStatus.Cancelled;
	}
	
	@Override
	public final void cancelRequest() {
		Log.log(this, "Cancelled");
		requestState.notifyCancelled();
		epiloguedLatch.countDown();
	}
	
	@Override
	public final ServiceID getServiceID() {
		return service.getID();
	}
	
	@Override
	public final ServiceID getRequesterID() {
		return requester.getID();
	}
	
	@Override
	public final RequestStatus getRequestStatus() {
		return requestState.getRequestStatus();
	}
	
	protected final boolean tryEnqueueServeRequest(ServiceHandleCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceHandleCertificate");
		return requestState.tryEnqueueServeRequest();
	}
	
	protected final boolean tryEnqueueEpilogueRequest(ServiceHandleCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceHandleCertificate");
		return requestState.tryEnqueueEpilogueRequest();
	}
	
	protected abstract void onServeRequest(RequestContext ctx) throws InterruptedException;
	
	protected final void serveRequest(ServiceRequestHandlerCertificate cert, RequestContext ctx) throws InterruptedException {
		if (null == cert)
			throw new IllegalArgumentException("ServiceRequestHandlerCertificate");
		if (null == ctx)
			throw new IllegalArgumentException("RequestContext");
		if (requestState.tryServeRequest()) {
			try {
				Log.log(this, "Serving");
				onServeRequest(ctx);
				Log.log(this, "Served");
				requestState.notifyServed();
				getRequester().generalNotifyRequestComplete(this);
			} catch (InterruptedException e) {
				Log.log(this, "Serving interrupted");
				requestState.notifyServingInterrupt(e);
				getRequester().generalNotifyRequestFailure(this);
				throw e;
			} catch (Exception e) {
				Log.logServingException(this, e);
				requestState.notifyServingException(e);
				getRequester().generalNotifyRequestFailure(this);
			}
		}
		else {
			Log.log(this, "Could not serve");
		}
	}
	
	protected abstract void onEpilogueRequest(RequestContext ctx) throws InterruptedException;
	
	protected final void epilogueRequest(ServiceRequestHandlerCertificate cert, RequestContext ctx) throws InterruptedException {
		if (null == cert)
			throw new IllegalArgumentException("ServiceRequestHandlerCertificate");
		if (null == ctx)
			throw new IllegalArgumentException("RequestContext");
		if (requestState.tryEpilogueRequest()) {
			try {
				Log.log(this, "Epilogue");
				onEpilogueRequest(ctx);
				Log.log(this, "Epilogued");
				requestState.notifyEpilogued();
			} catch (InterruptedException e) {
				Log.log(this, "Epilogue interrupted");
				requestState.notifyEpilogueInterrupt(e);
				throw e;
			} catch (Exception e) {
				Log.logEpilogueException(this, e);
				requestState.notifyEpilogueException(e);
			}
		}
		else {
			Log.log(this, "Could not epilogue");
		}
		epiloguedLatch.countDown();
	}
	
	@Override
	public final void awaitEpilogued() throws InterruptedException {
		epiloguedLatch.await();
	}
	
	@Override
	public final void awaitEpilogued(long timeout, TimeUnit unit) throws InterruptedException {
		epiloguedLatch.await(timeout, unit);
	}
}
