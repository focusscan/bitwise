package bitwise.engine.service;

import bitwise.apps.App;
import bitwise.appservice.AppServiceCertificate;
import bitwise.devices.Driver;
import bitwise.devices.usbservice.UsbServiceCertificate;
import bitwise.engine.Thing;
import bitwise.engine.config.Configuration;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Service<R extends Request, H extends ServiceHandle<R, ?>> extends Thing<ServiceID> implements Requester {
	private final ServiceCertificate cert = new ServiceCertificate();
	private ServiceRequestHandler<R> requestHandler;
	private ObservableList<ServiceTask> serviceTasks = FXCollections.observableArrayList();
	private ObservableList<Request> outRequests = FXCollections.observableArrayList();
	private ObservableList<App<?, ?>> childServices = FXCollections.observableArrayList();
	private ObservableList<Driver<?, ?, ?>> childDrivers = FXCollections.observableArrayList();
	
	protected Service() {
		super(new ServiceID());
		requestHandler = new ServiceRequestHandler<>(this, cert);
	}
	
	protected final ServiceRequestHandler<R> getRequestHandler() {
		return requestHandler;
	}
	
	public abstract H getServiceHandle();
	
	protected abstract boolean onStartService();
	
	public final void startService(SupervisorCertificate supervisorCert) {
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		synchronized(requestHandler) {
			if (!requestHandler.serviceIsRunning()) {
				requestHandler.startRequestHandler(cert);
			}
		}
	}
	
	protected abstract void onStopService();
	
	public final void stopService(SupervisorCertificate supervisorCert) throws InterruptedException {
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		synchronized(requestHandler) {
			if (requestHandler.serviceIsRunning()) {
				Log.log(this, "Stopping request handler");
				requestHandler.stopRequestHandler(cert);
				Log.log(this, "Stopping service tasks");
				for (ServiceTask serviceTask : serviceTasks)
					serviceTask.stopTask(cert);
				Log.log(this,  "Stopping drivers");
				for (Driver<?, ?, ?> driver : childDrivers)
					Supervisor.getInstance().stopService(driver);
				Log.log(this, "Service stopped");
			}
		}
	}
	
	protected final void addServiceTask(ServiceTask serviceTask) {
		Log.log(this, "Adding task %s", serviceTask);
		serviceTasks.add(serviceTask);
		serviceTask.startTask(cert);
	}
	
	protected final void notifyServiceTaskDone(ServiceTask serviceTask) {
		Log.log(this, "Task %s done", serviceTask);
		if (!Configuration.getInstance().rememberDoneServiceTasks())
			serviceTasks.remove(serviceTask);
	}
	
	@Override
	public final void generalNotifyChildApp(AppServiceCertificate appCert, App<?, ?> in) {
		if (null == cert)
			throw new IllegalArgumentException("AppServiceCertificate");
		Log.log(this, "Adding child service %s", in);
		childServices.add(in);
	}
	
	@Override
	public final void generalNotifyChildDriver(UsbServiceCertificate usbCert, Driver<?, ?, ?> in) {
		if (null == cert)
			throw new IllegalArgumentException("UsbServiceCertificate");
		Log.log(this, "Adding child driver %s", in);
		childDrivers.add(in);
	}
	
	public final void generalNotifyRequestEnqueued(Request in) {
		Log.log(this, "(Inbound) request enqueued %s", in);
		outRequests.add(in);
	}
	
	protected abstract void onRequestComplete(Request in);
	
	@Override
	public final void generalNotifyRequestComplete(Request in) throws InterruptedException {
		Log.log(this, "(Outbound) request complete notification %s", in);
		if (!Configuration.getInstance().rememberDoneRequests())
			outRequests.remove(in);
		onRequestComplete(in);
		requestHandler.enqueueEpilogue(in);
	}
	
	@Override
	public void generalNotifyRequestFailure(Request in) {
		Log.log(this, "(Outbound) request failure notification %s", in);
		if (!Configuration.getInstance().rememberDoneRequests())
			outRequests.remove(in);
		onRequestComplete(in);
	}
}
