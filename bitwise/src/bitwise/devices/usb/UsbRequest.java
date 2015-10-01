package bitwise.devices.usb;

import bitwise.devices.usb.events.UsbRequestFinishedEvent;
import bitwise.engine.supervisor.Supervisor;

public abstract class UsbRequest implements Runnable {
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
	
	@Override
	public void run() {
		serveRequest();
		Supervisor.getEventBus().publishEventToBus(new UsbRequestFinishedEvent(this));
	}
	
	protected abstract void serveRequest();
}
