package bitwise.appservice;

import bitwise.appservice.app.App;
import bitwise.appservice.app.AppFactory;
import bitwise.appservice.app.AppHandle;
import bitwise.appservice.app.AppRequest;
import bitwise.engine.service.Request;
import bitwise.engine.service.Requester;
import bitwise.engine.service.Service;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
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
	
	public void addAppFactory(AppFactory<?, ?, ?> factory) {
		Log.log(this, "Adding factory %s", factory);
		factories.add(factory);
	}
	
	public <R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> H startApp(Requester requester, AppFactory<R, H, A> factory) {
		Log.log(this, "Starting app from factory %s", factory);
		A app = factory.makeApp(cert);
		requester.generalNotifyChildService(cert, app);
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
