package bitwise.engine.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import bitwise.engine.supervisor.Supervisor;
import bitwise.log.Log;

public final class ServiceRequestHandler {
	private final ServiceRequestHandlerCertificate cert = new ServiceRequestHandlerCertificate();
	private final BaseService<?> service;
	private final BlockingQueue<BaseRequest<?, ?>> incomingRequests = new LinkedBlockingQueue<>();;
	private Runnable requestTask = null;
	private Thread requestThread = null;
	
	protected ServiceRequestHandler(BaseService<?> in_service) {
		service = in_service;
		requestTask = new Runnable() {
			@Override
			public void run() {
				try {
					if (!service.onStartService()) {
						Log.log(service, "Start aborted");
						return;
					}
					Log.log(service, "ServiceRequestHandler started");
					while (!Thread.interrupted()) {
						BaseRequest<?, ?> request = incomingRequests.take();
						Log.log(service, "Next request %s", request);
						RequestContext ctx = new RequestContext();
						if (request.getRequestStatus() == RequestStatus.EnqueuedServe) {
							Log.log(service, "Serving %s", request);
							request.serveRequest(cert, ctx);
						}
						else if (request.getRequestStatus() == RequestStatus.EnqueuedEpilogue) {
							Log.log(service, "Epilogueing %s", request);
							request.epilogueRequest(cert, ctx);
						}
						else {
							Log.log(service, "Not touching %s (status %s)", request, request.getRequestStatus());
						}
						ctx.invalidate();
					}
				} catch (InterruptedException e) {
					// Exit gracefully
				} catch (Exception e) {
					System.out.println("ServiceRequestHandler exception:");
					e.printStackTrace();
				}
				try {
					service.stopTasksAndDrivers(cert);
				} catch (InterruptedException e) {
					Log.logException(service, e);
				}
				Supervisor.getInstance().notifyServiceStopped(cert, service);
				service.onStopService();
				Log.log(service, "ServiceRequestHandler finished");
			}
		};
	}
	
	protected BlockingQueue<BaseRequest<?, ?>> getIncomingRequests(ServiceHandleCertificate shCert) {
		if (null == shCert)
			throw new IllegalArgumentException("ServiceHandleCertificate");
		return incomingRequests;
	}
	
	protected synchronized boolean serviceIsRunning() {
		return (null != requestThread && requestThread.isAlive());
	}
	
	protected synchronized void startRequestHandler(ServiceCertificate in_cert) {
		if (null == in_cert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (!serviceIsRunning()) {
			requestThread = new Thread(requestTask);
			requestThread.setName(String.format("ServiceRequestConsumer (%s)", this));
			requestThread.start();
		}
	}
	
	protected synchronized void stopRequestHandler(ServiceCertificate in_cert) throws InterruptedException {
		if (null == in_cert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (serviceIsRunning()) {
			requestThread.interrupt();
			requestThread.join();
		}
	}
}
