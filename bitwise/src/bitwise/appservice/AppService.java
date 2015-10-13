package bitwise.appservice;

import bitwise.apps.BaseApp;
import bitwise.apps.BaseAppFactory;
import bitwise.apps.BaseAppHandle;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;
import bitwise.engine.service.BaseService;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class AppService extends BaseService<AppServiceHandle> {
	private final AppServiceCertificate cert = new AppServiceCertificate();
	private final AppServiceHandle serviceHandle;
	private final ObservableList<BaseAppFactory<?>> factories = FXCollections.observableArrayList();
	
	public AppService(SupervisorCertificate supervisorCert) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		serviceHandle = new AppServiceHandle(this);
	}
	
	public ObservableList<BaseAppFactory<?>> getAppFactoryList() {
		return factories;
	}
	
	public void addAppFactory(BaseAppFactory<?> factory) {
		AppService thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				factories.add(factory);
				Log.log(thing, "Added factory %s", factory);
			}
		});
	}
	
	public <H extends BaseAppHandle<?, ?>> H startApp(BaseRequester requester, BaseAppFactory<H> factory) {
		Log.log(this, "Starting app from factory %s", factory);
		BaseApp<H> app = factory.makeApp(cert);
		requester.generalNotifyChildApp(cert, app);
		Supervisor.getInstance().addService(app);
		Log.log(this, "Started app %s", app);
		return app.getServiceHandle();
	}
	
	@Override
	public AppServiceHandle getServiceHandle() {
		return serviceHandle;
	}

	@Override
	protected boolean onStartService() {
		return true;
	}

	@Override
	protected void onStopService() {
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
	}
}
