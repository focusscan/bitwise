package bitwise.devices.usb;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;

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
		try {
			serveRequest();
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Supervisor.getEventBus().publishEventToBus(new UsbRequestFinishedEvent(this));
	}
	
	protected abstract void serveRequest() throws UsbNotActiveException, UsbDisconnectedException, UsbException;
}
