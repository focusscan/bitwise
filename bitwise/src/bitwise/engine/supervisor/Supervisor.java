package bitwise.engine.supervisor;

import bitwise.MainCertificate;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceHandle;
import bitwise.appservice.AppServiceRequest;
import bitwise.appservice.app.AppFactory;
import bitwise.appservice.requests.AddAppFactory;
import bitwise.appservice.requests.AddAppFactoryRequester;
import bitwise.engine.service.Request;
import bitwise.engine.service.Service;
import bitwise.log.Log;
import bitwise.usbservice.UsbService;
import bitwise.usbservice.UsbServiceHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class Supervisor extends Service<SupervisorRequest, SupervisorHandle> implements AddAppFactoryRequester {
	private static Supervisor instance = null;
	public static Supervisor getInstance() {
		if (null == instance) {
			instance = new Supervisor();
			Log.log(instance, "Instance created");
		}
		return instance;
	}
	
	private final SupervisorHandle handle;
	private final SupervisorCertificate cert = new SupervisorCertificate();
	private final ObservableList<Service<?, ?>> services = FXCollections.observableArrayList();
	private final AppService appService;
	private final UsbService usbService;
	private boolean initialized = false;
	
	protected Supervisor() {
		super();
		handle = new SupervisorHandle(this);
		appService = new AppService(cert);
		usbService = new UsbService(cert);
	}
	
	public synchronized void initialize(MainCertificate mainCert) {
		if (null == mainCert)
			throw new IllegalArgumentException("MainCertificate");
		if (!initialized) {
			Log.log(this, "Initializing");
			addService(appService);
			addService(usbService);
			initialized = true;
			this.startService(cert);
			Log.log(this, "Initialized");
		}
	}
	
	public void addAppFactory(AppFactory<?, ?, ?> factory) throws InterruptedException {
		AppServiceHandle appService = getAppServiceHandle();
		AppServiceRequest request = appService.addAppFactory(this, factory);
		appService.enqueueRequest(request);
	}
	
	public AppServiceHandle getAppServiceHandle() {
		return appService.getServiceHandle();
	}
	
	public UsbServiceHandle getUsbServiceHandle() {
		return usbService.getServiceHandle();
	}
	
	public void addService(Service<?, ?> service) {
		services.add(service);
		Log.log(this, "Starting service %s", service);
		service.startService(cert);
	}
	
	public void stopAllServices(MainCertificate mainCert) throws InterruptedException {
		if (null == mainCert)
			throw new IllegalArgumentException("MainCertificate");
		Log.log(this, "Stopping all services");
		for (Service<?, ?> service : services) {
			Log.log(this, "Stopping service %s", service);
			service.stopService(cert);
		}
		this.stopService(cert);
		Log.log(this, "All services stopped");
	}
	
	@Override
	public SupervisorHandle getServiceHandle() {
		return handle;
	}

	@Override
	protected boolean onStartService() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onStopService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRequestComplete(Request in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyRequestComplete(AddAppFactory<?, ?, ?> in) {
		Log.log(this, "Added app factory %s", in.getAppFactory());
	}
}
