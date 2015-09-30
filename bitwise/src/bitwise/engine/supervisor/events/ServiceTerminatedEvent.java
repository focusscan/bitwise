package bitwise.engine.supervisor.events;

import bitwise.engine.eventbus.Event;
import bitwise.engine.supervisor.Service;


public final class ServiceTerminatedEvent extends Event {
	private final Service service;
	
	public ServiceTerminatedEvent(Service in_service) {
		super("Service Terminated");
		service = in_service;
		assert(null != service);
	}
	
	public Service getService() {
		return service;
	}
	
	@Override
	public String getDescription() {
		return String.format("%s terminated.", service);
	}
}
