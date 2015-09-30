package bitwise.engine.supervisor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class Service {
	private final ServiceID id;
	private final String name;
	private int cfgNumThreads;
	private ExecutorService executor = null;
	
	public Service(String in_name, int in_cfgNumThreads) {
		id = new ServiceID();
		name = in_name;
		cfgNumThreads = in_cfgNumThreads;
		assert(null != name);
	}
	
	public ServiceID getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCfgNumThreads() {
		return cfgNumThreads;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by serviceNumber
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("SVC<%08x> (%s)", id.getValue(), name);
	}
	
	protected ExecutorService getExecutor() {
		return executor;
	}
	
	public boolean isRunning() {
		return ((null != executor) && !executor.isShutdown());
	}
	
	public boolean isShutdown() {
		return (null == executor) ? true : executor.isShutdown();
	}
	
	public boolean isTerminated() {
		return (null == executor) ? true : executor.isTerminated();
	}
	
	public synchronized boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}
	
	public synchronized boolean start() {
		if (!isTerminated())
			return false;
		executor = Executors.newFixedThreadPool(cfgNumThreads);
		onStart();
		return true;
	}
	
	public synchronized boolean shutdown() {
		if (!isRunning())
			return false;
		executor.shutdown();
		onShutdown();
		return true;
	}
	
	public synchronized boolean shutdownNow() {
		if (isTerminated())
			return false;
		executor.shutdownNow();
		onShutdownNow();
		return true;
	}
	
	protected abstract void onStart();
	protected abstract void onShutdown();
	protected abstract void onShutdownNow();
}
