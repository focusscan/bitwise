package bitwise.apps;

import javafx.beans.property.BooleanProperty;

public interface Resource {
	public ResourceID getID();
	public void resourceClose();
	public BooleanProperty getResourceIsOpen();
}
