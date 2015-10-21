package bitwise.apps;

import bitwise.engine.service.BaseService;

public abstract class BaseApp<H extends BaseAppHandle<?, ?>> extends BaseService<H> {
	protected BaseApp() {
		super();
	}
}
