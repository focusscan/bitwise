package bitwise.devices.kinds.fullcamera;

public class StorageDevice {
	private final String name;
	private final int value;
	
	public StorageDevice(String in_name, int in_value) {
		name = in_name;
		value = in_value;
		assert(null != name);
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
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
		return Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
