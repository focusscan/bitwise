package bitwise.filesystem;

import bitwise.engine.ID;

public final class DirectoryID extends ID {
	private static int nextID = 1;
	private static synchronized int getNextID() {
		return nextID++;
	}
	
	protected DirectoryID() {
		super(getNextID());
	}
}
