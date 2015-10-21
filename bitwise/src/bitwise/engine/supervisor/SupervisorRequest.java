package bitwise.engine.supervisor;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class SupervisorRequest extends BaseRequest<Supervisor, BaseRequester> {
	protected SupervisorRequest(Supervisor in_service, BaseRequester in_requester) {
		super(in_service, in_requester);
	}
}
