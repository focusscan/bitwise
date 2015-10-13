package bitwise.appservice;

import bitwise.apps.BaseApp;
import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.appservice.requests.*;
import bitwise.engine.service.BaseServiceHandle;
import javafx.collections.ObservableList;

public final class AppServiceHandle extends BaseServiceHandle<AppServiceRequest<?>, AppService> {
	protected AppServiceHandle(AppService in_service) {
		super(in_service);
	}
	
	public ObservableList<BaseAppFactory<?, ?>> getAppFactoryList() {
		return getService().getAppFactoryList();
	}
	
	public <H extends BaseAppHandle<?, ?>, A extends BaseApp<H>> AddAppFactory<H, A> addAppFactory(AddAppFactoryRequester requester, BaseAppFactory<H, A> factory) {
		return new AddAppFactory<>(getService(), requester, factory);
	}
	
	public <H extends BaseAppHandle<?, ?>, A extends BaseApp<H>> StartApp<H, A> startApp(StartAppRequester requester, BaseAppFactory<H, A> factory) {
		return new StartApp<>(getService(), requester, factory);
	}
}
