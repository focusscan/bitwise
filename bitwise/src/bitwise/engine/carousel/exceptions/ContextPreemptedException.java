package bitwise.engine.carousel.exceptions;

import bitwise.engine.carousel.Context;

public class ContextPreemptedException extends ContextException {
	private static final long serialVersionUID = 6921935531328584913L;
	
	private final Context youth;
	private final Context elder;
	
	public ContextPreemptedException(Context in_youth, Context in_elder) {
		youth = in_youth;
		elder = in_elder;
		assert(null != youth);
		assert(null != elder);
	}
	
	public Context getYouth() {
		return youth;
	}
	
	public Context getElder() {
		return elder;
	}
}
