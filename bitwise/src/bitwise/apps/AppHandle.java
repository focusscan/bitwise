package bitwise.apps;

import bitwise.engine.service.ServiceHandle;

public abstract class AppHandle<R extends AppRequest, A extends App<R, ?>> extends ServiceHandle<R, A> {
	protected AppHandle(A in_service) {
		super(in_service);
	}
}
