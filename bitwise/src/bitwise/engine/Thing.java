package bitwise.engine;

public class Thing<I extends ID> {
	private final I id;
	
	protected Thing(I in_id) {
		id = in_id;
		assert(null != id);
	}
	
	public I getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Thing<?>))
			return false;
		Thing<?> that = (Thing<?>)o;
		return id.equals(that.getID());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s<%02x>", this.getClass().getSimpleName(), id.getValue());
	}
}
