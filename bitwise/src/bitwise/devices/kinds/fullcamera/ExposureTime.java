package bitwise.devices.kinds.fullcamera;

public class ExposureTime {
	private final String name;
	private final int value;
	private final boolean setByUser;
	
	public ExposureTime(String in_name, int in_value, boolean in_setByUser) {
		name = in_name;
		value = in_value;
		setByUser = in_setByUser;
		assert(null != name);
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isSetByUser() {
		return setByUser;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExposureTime))
			return false;
		ExposureTime that = (ExposureTime) o;
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
