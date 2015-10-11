package bitwise.appservice;

import bitwise.appservice.app.App;
import bitwise.appservice.app.AppFactory;
import bitwise.appservice.app.AppHandle;
import bitwise.appservice.app.AppRequest;
import bitwise.appservice.requests.*;
import bitwise.engine.service.ServiceHandle;

public final class AppServiceHandle extends ServiceHandle<AppServiceRequest, AppService> {
	protected AppServiceHandle(AppService in_service) {
		super(in_service);
	}
	
	public <R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> AddAppFactory<R, H, A> addAppFactory(AddAppFactoryRequester requester, AppFactory<R, H, A> factory) {
		return new AddAppFactory<>(getService(), requester, factory);
	}
	
	public <R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> StartApp<R, H, A> startApp(StartAppRequester requester, AppFactory<R, H, A> factory) {
		return new StartApp<>(getService(), requester, factory);
	}
}
