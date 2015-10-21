package bitwise.engine.service;

import java.util.concurrent.TimeUnit;

public interface Request {
	public boolean isCancelled();
	public void cancelRequest();
	public ServiceID getServiceID();
	public ServiceID getRequesterID();
	public RequestStatus getRequestStatus();
	public void awaitEpilogued() throws InterruptedException;
	public void awaitEpilogued(long timeout, TimeUnit unit) throws InterruptedException;
}
