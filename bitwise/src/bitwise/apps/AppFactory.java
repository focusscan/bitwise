package bitwise.apps;

public abstract class AppFactory<A extends App> {
	private final AppFactoryID id;
	
	public AppFactory() {
		id = new AppFactoryID();
	}
	
	public final AppFactoryID getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id.
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("AppFactory<%08x> (%s)", id.getValue(), getName());
	}
	
	public abstract String getName();
	protected abstract A makeApp();
}
