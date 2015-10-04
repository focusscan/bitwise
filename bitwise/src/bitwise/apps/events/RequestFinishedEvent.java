package bitwise.apps.events;

import bitwise.apps.Request;
import bitwise.engine.eventbus.Event;


public abstract class RequestFinishedEvent<R extends Request> extends Event {
	private final R req;
	
	public RequestFinishedEvent(String in_name, R in_req) {
		super(in_name);
		req = in_req;
		assert(null != req);
	}
	
	public R getRequest() {
		return req;
	}
}
