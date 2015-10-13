package bitwise.engine.supervisor;

import bitwise.MainCertificate;
import bitwise.apps.BaseAppFactory;
import bitwise.appservice.AppService;
import bitwise.appservice.AppServiceHandle;
import bitwise.appservice.AppServiceRequest;
import bitwise.appservice.requests.AddAppFactory;
import bitwise.appservice.requests.AddAppFactoryRequester;
import bitwise.appservice.requests.StartApp;
import bitwise.appservice.requests.StartAppRequester;
import bitwise.devices.usbservice.UsbDriverFactory;
import bitwise.devices.usbservice.UsbService;
import bitwise.devices.usbservice.UsbServiceHandle;
import bitwise.devices.usbservice.UsbServiceRequest;
import bitwise.devices.usbservice.requests.AddUsbDriverFactory;
import bitwise.devices.usbservice.requests.AddUsbDriverFactoryRequester;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseService;
import bitwise.engine.service.ServiceRequestHandlerCertificate;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class Supervisor extends BaseService<SupervisorHandle> implements AddAppFactoryRequester, StartAppRequester, AddUsbDriverFactoryRequester {
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
	private final ObservableList<BaseService<?>> services = FXCollections.observableArrayList();
	private final AppService appService;
	private final UsbService usbService;
	private boolean initialized = false;
	
	protected Supervisor() {
		super();
		handle = new SupervisorHandle(this);
		appService = new AppService(cert);
		usbService = new UsbService(cert);
	}
	
	public ObservableList<BaseService<?>> getServicesList() {
		return services;
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
	
	public void addAppFactory(MainCertificate mainCert, BaseAppFactory<?, ?> factory) {
		AppServiceHandle appService = getAppServiceHandle();
		AppServiceRequest<?> request = appService.addAppFactory(this, factory);
		appService.enqueueRequest(request);
	}
	
	public void startApp(MainCertificate mainCert, BaseAppFactory<?, ?> factory) {
		AppServiceHandle appService = getAppServiceHandle();
		AppServiceRequest<?> request = appService.startApp(this, factory);
		appService.enqueueRequest(request);
	}
	
	public void addUsbDriverFactory(MainCertificate mainCert, UsbDriverFactory<?, ?> factory) {
		UsbServiceHandle usbService = getUsbServiceHandle();
		UsbServiceRequest<?> request = usbService.addUsbDriverFactory(this, factory);
		usbService.enqueueRequest(request);
	}
	
	public AppServiceHandle getAppServiceHandle() {
		return appService.getServiceHandle();
	}
	
	public UsbServiceHandle getUsbServiceHandle() {
		return usbService.getServiceHandle();
	}
	
	public void addService(BaseService<?> service) {
		Supervisor thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				services.add(service);
				Log.log(thing, "Starting service %s", service);
				service.startService(cert);
			}
		});
	}
	
	public void notifyServiceStopped(ServiceRequestHandlerCertificate serviceCert, BaseService<?> service) {
		if (null == serviceCert)
			throw new IllegalArgumentException("ServiceRequestHandlerCertificate");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				services.remove(service);
			}
		});
	}
	
	public void stopService(BaseService<?> service) throws InterruptedException {
		service.stopService(cert);
	}
	
	public void stopAllServices(MainCertificate mainCert) throws InterruptedException {
		if (null == mainCert)
			throw new IllegalArgumentException("MainCertificate");
		Log.log(this, "Stopping all services");
		for (BaseService<?> service : services) {
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
	protected void onRequestComplete(BaseRequest<?, ?> in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyRequestComplete(AddAppFactory<?, ?> in) {
		Log.log(this, "Added app factory %s", in.getAppFactory());
	}

	@Override
	public void notifyRequestComplete(StartApp<?, ?> in) {
		Log.log(this, "App started %s", in.getAppFactory());
	}

	@Override
	public void notifyRequestComplete(AddUsbDriverFactory<?, ?> in) {
		Log.log(this, "Added driver factory %s", in.getDriverFactory());
	}
}
