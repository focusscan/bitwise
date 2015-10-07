package bitwise.devices.kinds.fullcamera;

public class FNumber {
	private final short value;
	private final boolean setByUser;
	
	public FNumber(short in_value, boolean in_setByUser) {
		value = in_value;
		setByUser = in_setByUser;
	}
	
	public short getValue() {
		return value;
	}
	
	public boolean isSetByUser() {
		return setByUser;
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
		int whole = value / 100;
		int part  = value % 100;
		return String.format("f/%s.%s", whole, part);
	}
}
