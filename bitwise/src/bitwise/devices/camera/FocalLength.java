package bitwise.devices.camera;

public class FocalLength {
	private final String label;
	private final int value;
	
	public FocalLength(String in_label, int in_value) {
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
		if (!(o instanceof FocalLength))
			return false;
		FocalLength that = (FocalLength) o;
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
