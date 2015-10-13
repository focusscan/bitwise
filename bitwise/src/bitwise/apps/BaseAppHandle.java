package bitwise.apps;

import bitwise.engine.service.BaseServiceHandle;

public abstract class BaseAppHandle<R extends BaseAppRequest<?, ?>, A extends BaseApp<?>> extends BaseServiceHandle<R, A> {
	protected BaseAppHandle(A in_service) {
		super(in_service);
	}
}
