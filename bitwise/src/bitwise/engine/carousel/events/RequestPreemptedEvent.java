package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Request;
import bitwise.engine.eventbus.Event;


public final class RequestPreemptedEvent extends Event {
	private final Request request;
	private final Request preemptedBy;
	
	public RequestPreemptedEvent(Request in_request, Request in_preemptedBy) {
		super("Carousel Request Preempted");
		request = in_request;
		preemptedBy = in_preemptedBy;
		assert(null != request);
		assert(null != in_preemptedBy);
	}
	
	public Request getRequest() {
		return request;
	}
	
	public Request getPreemptedBy() {
		return preemptedBy;
	}

	@Override
	public String getDescription() {
		return String.format("%s preempted during execution by %s.", request, preemptedBy);
	}
}
