package bitwise.devices.camera;

public class ExposureTime {
	private final String label;
	private final int value;
	
	public ExposureTime(String in_label, int in_value) {
		label = in_label;
		value = in_value;
	}
	
	public int getValue() {
		return value;
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
		return label;
	}
}
