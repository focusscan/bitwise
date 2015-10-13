package bitwise.appservice.requests;

import bitwise.apps.BaseApp;
import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceRequest;
import bitwise.engine.service.RequestContext;

public final class StartApp<H extends BaseAppHandle<?, ?>, A extends BaseApp<H>> extends AppServiceRequest<StartAppRequester> {
	private final BaseAppFactory<H, A> factory;
	private H handle = null;
	
	public StartApp(AppService in_service, StartAppRequester in_requester, BaseAppFactory<H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public BaseAppFactory<H, A> getAppFactory() {
		return factory;
	}
	
	public H getHandle() {
		return handle;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		handle = getService().startApp(getRequester(), factory);
	}
	
	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
