package bitwise.devices.usb;

public final class UsbContext {
	private final UsbContextID id;
	
	protected UsbContext() {
		id = new UsbContextID();
	}
	
	public UsbContextID getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("UsbContext<%08x>", id.getValue());
	}
}
