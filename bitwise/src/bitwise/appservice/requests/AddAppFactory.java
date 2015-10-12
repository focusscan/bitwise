package bitwise.appservice.requests;

import bitwise.apps.App;
import bitwise.apps.AppFactory;
import bitwise.apps.AppHandle;
import bitwise.apps.AppRequest;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceRequest;
import bitwise.engine.service.RequestContext;
import bitwise.engine.service.requests.BaseRequest;

public final class AddAppFactory<R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> extends BaseRequest<AppService, AddAppFactoryRequester> implements AppServiceRequest {
	private final AppFactory<R, H, A> factory;
	
	public AddAppFactory(AppService in_service, AddAppFactoryRequester in_requester, AppFactory<R, H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public AppFactory<R, H, A> getAppFactory() {
		return factory;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		getService().addAppFactory(factory);
	}
	
	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
