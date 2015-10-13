package bitwise.devices.kinds.fullcamera;

public class FNumber {
	private final short value;
	private final String name;
	
	public FNumber(short in_value) {
		value = in_value;
		name = "";
	}
	
	public FNumber(String in_name, short in_value) {
		name = in_name;
		value = in_value;
		assert(null != name);
	}
	
	public short getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FNumber))
			return false;
		FNumber that = (FNumber) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		if (name.isEmpty()) {
			int whole = value / 100;
			int part  = value % 100;
			return String.format("f/%s.%s", whole, part);
		} else {
			return name;
		}
	}
}
