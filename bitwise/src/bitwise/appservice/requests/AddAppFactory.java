package bitwise.appservice.requests;

import bitwise.apps.BaseApp;
import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceRequest;
import bitwise.engine.service.RequestContext;

public final class AddAppFactory<H extends BaseAppHandle<?, ?>, A extends BaseApp<H>> extends AppServiceRequest<AddAppFactoryRequester> {
	private final BaseAppFactory<H, A> factory;
	
	public AddAppFactory(AppService in_service, AddAppFactoryRequester in_requester, BaseAppFactory<H, A> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public BaseAppFactory<H, A> getAppFactory() {
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
