package bitwise.apps;

import java.util.Collection;

import javafx.beans.property.BooleanProperty;

public interface Request {
	public RequestID getID();
	public void requestCancel();
	public BooleanProperty getRequestCanceled();
	public BooleanProperty getRequestServed();
	public BooleanProperty getCompletedSuccessfully();
	public abstract Collection<Resource> getNewResources();
}
