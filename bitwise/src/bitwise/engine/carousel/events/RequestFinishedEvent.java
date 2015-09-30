package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Request;
import bitwise.engine.eventbus.Event;


public final class RequestFinishedEvent extends Event {
	private final Request request;
	private final boolean publishChanges;
	
	public RequestFinishedEvent(Request in_request, boolean in_publishChanges) {
		super("Carousel Request Finished");
		request = in_request;
		publishChanges = in_publishChanges;
		assert(null != request);
	}
	
	public Request getRequest() {
		return request;
	}
	
	public boolean publishedChanged() {
		return publishChanges;
	}

	@Override
	public String getDescription() {
		return String.format("%s finished execution. %s",
				request,
				publishChanges ? "Changes published as per request." : "Changes NOT published as per request.");
	}
}
