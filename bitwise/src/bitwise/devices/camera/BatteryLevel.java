package bitwise.devices.camera;

public class BatteryLevel {
	private final String label;
	private final byte value;
	
	public BatteryLevel(String in_label, byte in_value) {
		label = in_label;
		value = in_value;
	}
	
	public byte getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BatteryLevel))
			return false;
		BatteryLevel that = (BatteryLevel) o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return label;
	}
}
