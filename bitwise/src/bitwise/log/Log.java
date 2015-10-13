package bitwise.log;

import bitwise.engine.Thing;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseService;
import bitwise.engine.service.BaseServiceTask;
import bitwise.engine.supervisor.Supervisor;

public final class Log {
	private Log() {
		
	}
	
	private static void log(String kind, Thing<?> thing, String format, Object... args) {
		System.out.println(String.format("[%8s] %24s - %s", kind, thing, String.format(format, args)));
	}
	
	public static void log(Thing<?> thing, String format, Object... args) {
		log("Thing", thing, format, args);
	}
	
	public static void log(BaseService<?> thing, String format, Object... args) {
		log("Service", thing, format, args);
	}
	
	public static void log(BaseServiceTask thing, String format, Object... args) {
		log("Task", thing, format, args);
	}
	
	public static void log(BaseRequest<?, ?> thing, String format, Object... args) {
		log("Request", thing, format, args);
	}
	
	public static void log(Supervisor thing, String format, Object... args) {
		log("Super", thing, format, args);
	}
	
	public static void logServingException(BaseRequest<?, ?> thing, Exception e) {
		log(thing, "Serving exception: %s", e);
		e.printStackTrace();
	}
	
	public static void logEpilogueException(BaseRequest<?, ?> thing, Exception e) {
		log(thing, "Epilogue exception: %s", e);
		e.printStackTrace();
	}
}
