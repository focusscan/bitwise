package bitwise.engine.service;

public final class RequestState {
	private RequestStatus status = RequestStatus.Created;
	private InterruptedException servingInterrupt = null;
	private Exception servingException = null;
	private InterruptedException epilogueInterrupt = null;
	private Exception epilogueException = null;
	
	protected RequestState() {
		
	}
	
	public RequestStatus getRequestStatus() {
		return status;
	}
	
	protected synchronized void notifyCancelled() {
		status = RequestStatus.Cancelled;
	}
	
	protected synchronized boolean tryEnqueueServeRequest() {
		if (RequestStatus.Created == status) {
			status = RequestStatus.EnqueuedServe;
			return true;
		}
		return false;
	}
	
	protected synchronized boolean tryServeRequest() {
		if (RequestStatus.EnqueuedServe == status) {
			status = RequestStatus.Serving;
			return true;
		}
		return false;
	}
	
	public InterruptedException getServingInterrupt() {
		return servingInterrupt;
	}
	
	public Exception getServingException() {
		return servingException;
	}
	
	protected synchronized void notifyServingInterrupt(InterruptedException in_interrupt) {
		status = RequestStatus.ServingInterrupted;
		servingInterrupt = in_interrupt;
	}
	
	protected synchronized void notifyServingException(Exception in_exception) {
		status = RequestStatus.ServingException;
		servingException = in_exception;
	}
	
	protected synchronized void notifyServed() {
		status = RequestStatus.Served;
	}
	
	protected synchronized boolean tryEnqueueEpilogueRequest() {
		if (RequestStatus.Served == status || RequestStatus.ServingInterrupted == status || RequestStatus.ServingException == status) {
			status = RequestStatus.EnqueuedEpilogue;
			return true;
		}
		return false;
	}
	
	protected synchronized boolean tryEpilogueRequest() {
		if (RequestStatus.EnqueuedEpilogue == status) {
			status = RequestStatus.Epilogue;
			return true;
		}
		return false;
	}
	
	public InterruptedException getEpilogueInterrupt() {
		return epilogueInterrupt;
	}
	
	public Exception getEpilogueException() {
		return epilogueException;
	}
	
	protected synchronized void notifyEpilogued() {
		status = RequestStatus.Epilogued;
	}
	
	protected synchronized void notifyEpilogueInterrupt(InterruptedException in_interrupt) {
		status = RequestStatus.EpilogueInterrupted;
		epilogueInterrupt = in_interrupt;
	}
	
	protected synchronized void notifyEpilogueException(Exception in_exception) {
		status = RequestStatus.EpilogueException;
		epilogueException = in_exception;
	}
}
