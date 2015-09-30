package bitwise.devices.usb;

public abstract class UsbDriverFactory<D extends UsbDriver> {
	private final UsbDriverFactoryID id;
	
	public UsbDriverFactory() {
		id = new UsbDriverFactoryID();
	}
	
	public final UsbDriverFactoryID getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id.
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("UsbDriverFactory<%08x> (%s)", id.getValue(), getName());
	}
	
	public abstract String getName();
	public abstract Class<D> getDriverClass();
	public abstract boolean isCompatibleWith(UsbDevice in);
	protected abstract D makeDriver(UsbDevice in);
}
