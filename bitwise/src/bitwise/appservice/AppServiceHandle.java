package bitwise.appservice;

import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.appservice.requests.*;
import bitwise.engine.service.BaseServiceHandle;
import javafx.collections.ObservableList;

public final class AppServiceHandle extends BaseServiceHandle<AppServiceRequest<?>, AppService> {
	protected AppServiceHandle(AppService in_service) {
		super(in_service);
	}
	
	public ObservableList<BaseAppFactory<?>> getAppFactoryList() {
		return getService().getAppFactoryList();
	}
	
	public <H extends BaseAppHandle<?, ?>> AddAppFactory<H> addAppFactory(AddAppFactoryRequester requester, BaseAppFactory<H> factory) {
		AddAppFactory<H> request = new AddAppFactory<>(getService(), requester, factory);
		this.enqueueRequest(request);
		return request;
	}
	
	public <H extends BaseAppHandle<?, ?>> StartApp<H> startApp(StartAppRequester requester, BaseAppFactory<H> factory) {
		StartApp<H> request = new StartApp<>(getService(), requester, factory);
		this.enqueueRequest(request);
		return request;
	}
}
