package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Request;
import bitwise.engine.eventbus.Event;


public final class RequestRunningEvent extends Event {
	private final Request request;
	
	public RequestRunningEvent(Request in_request) {
		super("Carousel Request Running");
		request = in_request;
		assert(null != request);
	}
	
	public Request getRequest() {
		return request;
	}

	@Override
	public String getDescription() {
		return String.format("%s executing.", request);
	}
}
