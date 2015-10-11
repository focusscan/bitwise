package bitwise.engine.service;

import bitwise.engine.service.requests.RequestID;
import bitwise.engine.service.requests.RequestStatus;

public interface Request {
	public RequestID getID();
	public ServiceID getServiceID();
	public ServiceID getRequesterID();
	public RequestStatus getRequestStatus();
	
	public boolean tryEnqueueServeRequest(ServiceCertificate cert);
	public void serveRequest(ServiceCertificate cert, RequestContext ctx);
	public boolean tryEnqueueEpilogueRequest(ServiceCertificate cert);
	public void epilogueRequest(ServiceCertificate cert, RequestContext ctx);
}
