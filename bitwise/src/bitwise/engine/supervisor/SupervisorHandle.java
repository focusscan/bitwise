package bitwise.engine.supervisor;

import bitwise.engine.service.BaseServiceHandle;

public final class SupervisorHandle extends BaseServiceHandle<SupervisorRequest, Supervisor> {
	protected SupervisorHandle(Supervisor in_service) {
		super(in_service);
	}
}
