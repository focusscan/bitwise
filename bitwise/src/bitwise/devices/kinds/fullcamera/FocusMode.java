package bitwise.devices.kinds.fullcamera;

public class FocusMode {
	private final String name;
	private final short value;
	private final boolean setByUser;
	
	public FocusMode(String in_name, short in_value, boolean in_setByUser) {
		name = in_name;
		value = in_value;
		setByUser = in_setByUser;
		assert(null != name);
	}
	
	public String getName() {
		return name;
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
		if (!(o instanceof FocusMode))
			return false;
		FocusMode that = (FocusMode) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
