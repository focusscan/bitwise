package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Request;
import bitwise.engine.eventbus.Event;


public final class RequestAbortedEvent extends Event {
	private final Request request;
	
	public RequestAbortedEvent(Request in_request) {
		super("Carousel Request Aborted");
		request = in_request;
		assert(null != request);
	}
	
	public Request getRequest() {
		return request;
	}

	@Override
	public String getDescription() {
		return String.format("%s aborted during execution.", request);
	}
}
