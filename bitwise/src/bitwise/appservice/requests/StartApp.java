package bitwise.appservice.requests;

import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceRequest;
import bitwise.appservice.app.App;
import bitwise.appservice.app.AppFactory;
import bitwise.appservice.app.AppHandle;
import bitwise.appservice.app.AppRequest;
import bitwise.engine.service.RequestContext;
import bitwise.engine.service.requests.BaseRequest;

public final class StartApp<R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> extends BaseRequest<AppService, StartAppRequester> implements AppServiceRequest {
	private final AppFactory<R, H, A> factory;
	private H handle = null;
	
	public StartApp(AppService in_service, StartAppRequester in_requester, AppFactory<R, H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public AppFactory<R, H, A> getAppFactory() {
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
