package bitwise.engine;

public abstract class ID {
	private int value;
	
	private static int nextValue = 1;
	private synchronized static int getNextValue() {
		return nextValue++;
	}
	
	protected ID(int in_value) {
		// value = in_value;
		value = getNextValue();
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o);
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("%s<%08x>", this.getClass().getName(), value);
	}
}
