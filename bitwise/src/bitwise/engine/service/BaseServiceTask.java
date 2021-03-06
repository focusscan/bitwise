package bitwise.engine.service;

import bitwise.engine.Thing;
import bitwise.log.Log;

public abstract class BaseServiceTask<S extends BaseService<?>> extends Thing<ServiceTaskID> implements Runnable {
	private final S service;
	private Thread thread = null;
	
	public BaseServiceTask(S in_service) {
		super(new ServiceTaskID());
		service = in_service;
	}
	
	protected final S getService() {
		return service;
	}
	
	protected final void startTask(ServiceCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("ServiceCertificate");
		if (null == thread) {
			thread = new Thread(this);
			thread.setName(String.format("%s:%s", service, this));
			thread.start();
		}
	}
	
	private volatile boolean cancelled = false;
	
	public final void cancel() {
		cancelled = true;
		Log.log(this, "Task cancelled");
	}
	
	public final boolean isCancelled() {
		return cancelled;
	}
	
	protected final void stopTask(ServiceCertificate cert) throws InterruptedException {
		cancelled = true;
		if (null != thread) {
			Log.log(this, "Stopping task");
			thread.interrupt();
			thread.join();
			Log.log(this, "Task stopped");
		}
	}
	
	protected abstract void taskMain() throws InterruptedException;
	
	private boolean running = false;
	
	public final boolean isRunning() {
		return running;
	}
	
	@Override
	public final void run() {
		if (!cancelled) {
			Log.log(this, "Running task");
			running = true;
			try {
				taskMain();
			} catch (InterruptedException e) {
			} catch (Exception e) {
				Log.logException(this, e);
			}
			running = false;
			Log.log(this, "Task finished");
		}
		service.notifyServiceTaskDone(this);
	}
}
