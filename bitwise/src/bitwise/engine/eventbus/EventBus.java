package bitwise.engine.eventbus;


public final class EventBus {
	private EventNode eventNode = new EventNode(new EventBusHelloEvent());
	
	public EventBus() {
		
	}
	
	public synchronized EventNode getEventNode() {
		return eventNode;
	}
	
	public synchronized void publishEventToBus(Event in) {
		EventNode old_eventNode = eventNode;
		eventNode = new EventNode(in);
		old_eventNode.setNext(eventNode);
	}
}
