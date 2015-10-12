package bitwise.devices;

import bitwise.engine.service.ServiceHandle;

public abstract class DriverHandle<R extends DriverRequest, A extends Driver<?, R, ?>> extends ServiceHandle<R, A> {
	protected DriverHandle(A in_service) {
		super(in_service);
	}
}
