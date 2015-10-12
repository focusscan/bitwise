package bitwise.devices.camera;

public class StorageDevice {
	private final String label;
	private final short value;
	
	public StorageDevice(String in_label, short in_value) {
		label = in_label;
		value = in_value;
	}
	
	public short getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof StorageDevice))
			return false;
		StorageDevice that = (StorageDevice) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return label;
	}
}
