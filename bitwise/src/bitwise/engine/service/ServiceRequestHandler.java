package bitwise.engine.service;

import java.util.concurrent.ArrayBlockingQueue;

import bitwise.engine.config.Configuration;
import bitwise.engine.service.requests.RequestStatus;
import bitwise.log.Log;

public final class ServiceRequestHandler<R extends Request> {
	private final Service<R, ?> service;
	private final ServiceCertificate cert;
	private final ArrayBlockingQueue<Request> incomingRequests;
	private Runnable requestTask = null;
	private Thread requestThread = null;
	
	protected ServiceRequestHandler(Service<R, ?> in_service, ServiceCertificate in_cert) {
		service = in_service;
		cert = in_cert;
		incomingRequests = new ArrayBlockingQueue<>(Configuration.getInstance().getIncomingRequestQueueSize());
		requestTask = new Runnable() {
			@Override
			public void run() {
				try {
					if (!service.onStartService())
						return;
					Log.log(service, "ServiceRequestHandler started");
					while (!Thread.interrupted()) {
						Request request = incomingRequests.take();
						RequestContext ctx = new RequestContext();
						if (request.getRequestStatus() == RequestStatus.Served)
							request.epilogueRequest(cert, ctx);
						else
							request.serveRequest(cert, ctx);
						ctx.invalidate();
					}
				} catch (InterruptedException e) {
					// Exit gracefully
				} catch (Exception e) {
					System.out.println("ServiceRequestHandler exception:");
					e.printStackTrace();
				}
				service.onStopService();
				Log.log(service, "ServiceRequestHandler finished");
			}
		};
	}
	
	protected void enqueueRequest(R in) throws InterruptedException {
		if (in.tryEnqueueServeRequest(cert))
			incomingRequests.put(in);
	}
	
	protected void enqueueEpilogue(Request in) throws InterruptedException {
		if (in.tryEnqueueEpilogueRequest(cert))
			incomingRequests.put(in);
	}
	
	public synchronized boolean serviceIsRunning() {
		return (null != requestThread && requestThread.isAlive());
	}
	
	protected synchronized void startRequestHandler(ServiceCertificate in_cert) {
		if (in_cert != cert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (!serviceIsRunning()) {
			requestThread = new Thread(requestTask);
			requestThread.setName(String.format("ServiceRequestConsumer (%s)", this));
			requestThread.start();
		}
	}
	
	protected synchronized void stopRequestHandler(ServiceCertificate in_cert) throws InterruptedException {
		if (in_cert != cert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (serviceIsRunning()) {
			requestThread.interrupt();
			requestThread.join();
		}
	}
}
