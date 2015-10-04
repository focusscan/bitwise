package bitwise.devices.usb;

import bitwise.apps.Request;
import bitwise.devices.usb.events.UsbRequestFinishedEvent;
import bitwise.engine.supervisor.Supervisor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class UsbRequest implements Request, Runnable {
	private final UsbRequestID id;
	private final String name;
	
	public UsbRequest(String in_name) {
		id = new UsbRequestID();
		name = in_name;
		assert(null != name);
	}
	
	public UsbRequestID getID() {
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
		return String.format("USBREQ<%08x> (%s)", id.getValue(), name);
	}
	
	private BooleanProperty requestCanceled = new SimpleBooleanProperty(false);
	private BooleanProperty requestServed = new SimpleBooleanProperty(false);
	private BooleanProperty completedSuccessfully = new SimpleBooleanProperty(false);
	
	@Override
	public synchronized final void requestCancel() {
		if (!requestServed.get())
			requestCanceled.set(true);
	}
	
	@Override
	public final BooleanProperty getRequestCanceled() {
		return requestCanceled;
	}
	
	@Override
	public final BooleanProperty getRequestServed() {
		return requestServed;
	}
	
	@Override
	public final BooleanProperty getCompletedSuccessfully() {
		return completedSuccessfully;
	}
	
	@Override
	public void run() {
		UsbContext ctx = new UsbContext();
		serveRequest(ctx);
		synchronized (this) {
			completedSuccessfully.set(true);
			requestServed.set(true);
		}
		Supervisor.getEventBus().publishEventToBus(new UsbRequestFinishedEvent(this));
	}
	
	protected abstract void serveRequest(UsbContext ctx);
}
