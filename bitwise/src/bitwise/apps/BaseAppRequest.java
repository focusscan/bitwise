package bitwise.apps;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class BaseAppRequest<A extends BaseApp<?>, R extends BaseRequester> extends BaseRequest<A, R> {
	protected BaseAppRequest(A in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
