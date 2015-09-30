package bitwise.engine;

public abstract class ID {
	private final int value;
	
	public ID(int id_value) {
		value = id_value;
	}
	
	public final int getValue() {
		return value;
	}
	
	public abstract String getIDKind();
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ID))
			return false;
		ID that = (ID)o;
		return this.value == that.value;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%s<%08x>", getIDKind(), value);
	}
}
