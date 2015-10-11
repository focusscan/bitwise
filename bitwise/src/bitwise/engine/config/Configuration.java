package bitwise.engine.config;

public class Configuration {
	private static Configuration instance = null;
	public static Configuration getInstance() {
		if (null == instance)
			instance = new Configuration();
		return instance;
	}
	
	protected Configuration() {
		
	}
	
	public int getIncomingRequestQueueSize() {
		return 16;
	}
	
	public boolean rememberInvalidContexts() {
		return true;
	}
	
	public boolean rememberDoneServiceTasks() {
		return true;
	}
	
	public boolean rememberDoneRequests() {
		return true;
	}
}
