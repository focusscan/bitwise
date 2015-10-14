package bitwise.engine.service;

import java.util.concurrent.TimeUnit;

public interface Request {
	public boolean isCancelled();
	public void cancelRequest();
	public ServiceID getServiceID();
	public ServiceID getRequesterID();
	public RequestStatus getRequestStatus();
	public void awaitServed() throws InterruptedException;
	public void awaitServed(long timeout, TimeUnit unit) throws InterruptedException;
}
