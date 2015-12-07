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
	
	public boolean rememberInvalidContexts() {
		return false;
	}
	
	public boolean rememberDoneServiceTasks() {
		return false;
	}
	
	public boolean rememberDoneRequests() {
		return false;
	}
}
