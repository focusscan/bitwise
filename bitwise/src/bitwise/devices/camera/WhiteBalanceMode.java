package bitwise.devices.camera;

public class WhiteBalanceMode {
	private final String label;
	private final short value;
	
	public WhiteBalanceMode(String in_label, short in_value) {
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
		if (!(o instanceof WhiteBalanceMode))
			return false;
		WhiteBalanceMode that = (WhiteBalanceMode) o;
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
