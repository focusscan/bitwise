package bitwise.engine.service;

import bitwise.apps.BaseApp;
import bitwise.appservice.AppServiceCertificate;
import bitwise.devices.BaseDriver;
import bitwise.devices.usbservice.UsbServiceCertificate;
import bitwise.engine.Thing;
import bitwise.engine.config.Configuration;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class BaseService<H extends BaseServiceHandle<?, ?>> extends Thing<ServiceID> implements BaseRequester {
	private final ServiceCertificate cert = new ServiceCertificate();
	private ServiceRequestHandler requestHandler;
	private ObservableList<BaseServiceTask<?>> serviceTasks = FXCollections.observableArrayList();
	// private ObservableList<BaseRequest<?, ?>> outRequests = FXCollections.observableArrayList();
	private ObservableList<BaseApp<?>> childServices = FXCollections.observableArrayList();
	private ObservableList<BaseDriver<?, ?>> childDrivers = FXCollections.observableArrayList();
	
	protected BaseService() {
		super(new ServiceID());
		requestHandler = new ServiceRequestHandler(this);
	}
	
	protected final ServiceRequestHandler getRequestHandler() {
		return requestHandler;
	}
	
	public abstract H getServiceHandle();
	
	protected abstract boolean onStartService() throws InterruptedException;
	
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
	
	protected final void stopTasksAndDrivers(ServiceRequestHandlerCertificate handlerCert) throws InterruptedException {
		if (null == handlerCert)
			throw new IllegalArgumentException("ServiceRequestHandlerCertificate");
		Log.log(this,  "Stopping drivers");
		for (BaseDriver<?, ?> driver : childDrivers)
			Supervisor.getInstance().stopService(driver);
		Log.log(this, "Stopping service tasks");
		for (BaseServiceTask<?> serviceTask : serviceTasks)
			serviceTask.stopTask(cert);
		Log.log(this, "Service stopped");
	}
	
	public final void stopService(SupervisorCertificate supervisorCert) throws InterruptedException {
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		stopService();
	}
	
	private final void stopService() throws InterruptedException {
		synchronized(requestHandler) {
			if (requestHandler.serviceIsRunning()) {
				Log.log(this, "Stopping request handler");
				requestHandler.stopRequestHandler(cert);
			}
		}
	}
	
	protected final void stopServiceFromWithin() {
		Thread stopThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stopService();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		stopThread.setName(String.format("stopServiceFromWithin %s", this));
		stopThread.setDaemon(true);
		stopThread.start();
	}
	
	protected final void addServiceTask(BaseServiceTask<?> serviceTask) {
		Log.log(this, "Adding task %s", serviceTask);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				serviceTasks.add(serviceTask);
			}
		});
		serviceTask.startTask(cert);
	}
	
	protected final void notifyServiceTaskDone(BaseServiceTask<?> serviceTask) {
		Log.log(this, "Task %s done", serviceTask);
		if (!Configuration.getInstance().rememberDoneServiceTasks())
			serviceTasks.remove(serviceTask);
	}
	
	@Override
	public final void generalNotifyChildApp(AppServiceCertificate appCert, BaseApp<?> in) {
		if (null == cert)
			throw new IllegalArgumentException("AppServiceCertificate");
		BaseService<?> thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				childServices.add(in);
				Log.log(thing, "Added child service %s", in);
			}
		});
	}
	
	@Override
	public final void generalNotifyChildDriver(UsbServiceCertificate usbCert, BaseDriver<?, ?> in) {
		if (null == cert)
			throw new IllegalArgumentException("UsbServiceCertificate");
		BaseService<?> thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				childDrivers.add(in);
				Log.log(thing, "Added child driver %s", in);
			}
		});
	}
	
	@Override
	public final void generalNotifyRequestEnqueued(BaseRequest<?, ?> in) {
		/*
		BaseService<?> thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				outRequests.add(in);
			}
		});
		*/
		Log.log(this, "(Inbound) request enqueued %s", in);
	}
	
	protected abstract void onRequestComplete(BaseRequest<?, ?> in);
	
	@Override
	public final void generalNotifyRequestComplete(BaseRequest<?, ?> in) {
		Log.log(this, "(Outbound) request complete notification %s", in);
		/*
		if (!Configuration.getInstance().rememberDoneRequests()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					outRequests.remove(in);
				}
			});
		}
		*/
		onRequestComplete(in);
		getServiceHandle().enqueueEpilogue(cert, in);
	}
	
	@Override
	public final void generalNotifyRequestFailure(BaseRequest<?, ?> in) {
		Log.log(this, "(Outbound) request failure notification %s", in);
		/*
		if (!Configuration.getInstance().rememberDoneRequests()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					outRequests.remove(in);
				}
			});
		}
		*/
		onRequestComplete(in);
		getServiceHandle().enqueueEpilogue(cert, in);
	}
}
