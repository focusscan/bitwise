package bitwise.engine.eventbus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class EventNode {
	private final Event event;
	private final CountDownLatch latch = new CountDownLatch(1);
	private EventNode next = null;
	
	public EventNode(Event in_event) {
		event = in_event;
		assert(null != event);
	}
	
	public Event getEvent() {
		return event;
	}
	
	public synchronized void setNext(EventNode in_next) {
		assert(null == next);
		next = in_next;
		assert(null != next);
		latch.countDown();
	}
	
	public EventNode waitLimitedOnNext(long time, TimeUnit unit) throws InterruptedException {
		if (latch.await(time, unit)) {
			assert(null != next);
			return next;
		}
		return null;
	}
	
	public EventNode waitOnNext() throws InterruptedException {
		latch.await();
		assert(null != next);
		return next;
	}
	
	public synchronized boolean nextEventReady() {
		return (null != next);
	}
}
