package bitwise.devices;

import bitwise.engine.service.BaseServiceHandle;

public abstract class BaseDriverHandle<R extends BaseDriverRequest<?, ?>, A extends BaseDriver<?, ?>> extends BaseServiceHandle<R, A> {
	protected BaseDriverHandle(A in_service) {
		super(in_service);
	}
}
