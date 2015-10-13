package bitwise.appservice.requests;

import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceRequest;
import bitwise.engine.service.RequestContext;

public final class AddAppFactory<H extends BaseAppHandle<?, ?>> extends AppServiceRequest<AddAppFactoryRequester> {
	private final BaseAppFactory<H> factory;
	
	public AddAppFactory(AppService in_service, AddAppFactoryRequester in_requester, BaseAppFactory<H> in_factory) {
		super(in_service, in_requester);
		factory = in_factory;
	}
	
	public BaseAppFactory<H> getAppFactory() {
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
