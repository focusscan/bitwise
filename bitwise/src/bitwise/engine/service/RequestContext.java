package bitwise.engine.service;

import bitwise.engine.Thing;

public class RequestContext extends Thing<RequestContextID> {
	private boolean mIsValid = true;
	
	protected RequestContext() {
		super(new RequestContextID());
	}
	
	public boolean isValid() {
		return mIsValid;
	}
	
	protected void invalidate() {
		mIsValid = false;
	}
}
