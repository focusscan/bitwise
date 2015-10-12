package bitwise.appservice;

import bitwise.apps.App;
import bitwise.apps.AppFactory;
import bitwise.apps.AppHandle;
import bitwise.apps.AppRequest;
import bitwise.engine.service.Request;
import bitwise.engine.service.Requester;
import bitwise.engine.service.Service;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class AppService extends Service<AppServiceRequest, AppServiceHandle> {
	private final AppServiceCertificate cert = new AppServiceCertificate();
	private final AppServiceHandle serviceHandle;
	private final ObservableList<AppFactory<?, ?, ?>> factories = FXCollections.observableArrayList();
	
	public AppService(SupervisorCertificate supervisorCert) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		serviceHandle = new AppServiceHandle(this);
	}
	
	public ObservableList<AppFactory<?, ?, ?>> getAppFactoryList() {
		return factories;
	}
	
	public void addAppFactory(AppFactory<?, ?, ?> factory) {
		AppService thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				factories.add(factory);
				Log.log(thing, "Added factory %s", factory);
			}
		});
	}
	
	public <R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> H startApp(Requester requester, AppFactory<R, H, A> factory) {
		Log.log(this, "Starting app from factory %s", factory);
		A app = factory.makeApp(cert);
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
	protected void onRequestComplete(Request in) {
	}
}
