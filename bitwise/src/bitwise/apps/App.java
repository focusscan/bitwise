package bitwise.apps;

import bitwise.engine.service.Service;

public abstract class App<R extends AppRequest, H extends AppHandle<R, ?>> extends Service<R, H> {
	protected App() {
		super();
	}
}
