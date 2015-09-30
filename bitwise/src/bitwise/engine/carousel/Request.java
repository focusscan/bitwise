package bitwise.engine.carousel;

import bitwise.engine.carousel.exceptions.ContextPreemptedException;

public abstract class Request {
	private final RequestID id;
	private final String name;
	
	public Request(String in_name) {
		id = new RequestID();
		name = in_name;
		assert(null != name);
	}
	
	public RequestID getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by requestNumber
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("REQ<%08x> (%s)", id.getValue(), name);
	}
	
	public abstract boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException;
}
