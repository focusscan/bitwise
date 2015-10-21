package bitwise.devices;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class BaseDriverRequest<A extends BaseDriver<?, ?>, R extends BaseRequester> extends BaseRequest<A, R> {
	protected BaseDriverRequest(A in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
