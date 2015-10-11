package bitwise.engine.supervisor;

import bitwise.engine.service.ServiceHandle;

public final class SupervisorHandle extends ServiceHandle<SupervisorRequest, Supervisor> {
	protected SupervisorHandle(Supervisor in_service) {
		super(in_service);
	}
}
