package bitwise.engine.service.requests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bitwise.engine.Thing;
import bitwise.engine.service.Request;
import bitwise.engine.service.RequestContext;
import bitwise.engine.service.Requester;
import bitwise.engine.service.Service;
import bitwise.engine.service.ServiceCertificate;
import bitwise.engine.service.ServiceID;
import bitwise.log.Log;

public abstract class BaseRequest<S extends Service<?, ?>, R extends Requester> extends Thing<RequestID> implements Request {
	private final S service;
	private final R requester;
	private final RequestState requestState = new RequestState();
	private CountDownLatch servedLatch = new CountDownLatch(1);
	
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
	
	public final boolean isCancelled() {
		return requestState.getRequestStatus() == RequestStatus.Cancelled;
	}
	
	public final void cancelRequest() {
		Log.log(this, "Cancelled");
		requestState.notifyCancelled();
		servedLatch.countDown();
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
	
	@Override
	public final boolean tryEnqueueServeRequest(ServiceCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceCertificate");
		return requestState.tryEnqueueServeRequest();
	}
	
	@Override
	public final boolean tryEnqueueEpilogueRequest(ServiceCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceCertificate");
		return requestState.tryEnqueueServeRequest();
	}
	
	protected abstract void onServeRequest(RequestContext ctx) throws InterruptedException;
	
	@Override
	public final void serveRequest(ServiceCertificate cert, RequestContext ctx) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceCertificate");
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
			} catch (Exception e) {
				Log.log(this, "Serving exception: %s\n%s", e, e.getStackTrace());
				requestState.notifyServingException(e);
				getRequester().generalNotifyRequestFailure(this);
			}
		}
		servedLatch.countDown();
	}
	
	protected abstract void onEpilogueRequest(RequestContext ctx) throws InterruptedException;
	
	@Override
	public final void epilogueRequest(ServiceCertificate cert, RequestContext ctx) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceCertificate");
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
			} catch (Exception e) {
				Log.log(this, "Epilogue exception: %s\n%s", e, e.getStackTrace());
				requestState.notifyEpilogueException(e);
			}
		}
	}
	
	public final void awaitServed() throws InterruptedException {
		servedLatch.await();
	}
	
	public final void awaitServed(long timeout, TimeUnit unit) throws InterruptedException {
		servedLatch.await(timeout, unit);
	}
}
