package bitwise.model.entity;

import bitwise.engine.ID;

public class EntityID extends ID {
	private static final String idKind = "EntityID";
	private static int NextValue = 1;	
	private static synchronized int nextValue() {
		return NextValue++;
	}
	
	public EntityID() {
		super(nextValue());
	}

	@Override
	public String getIDKind() {
		return idKind;
	}
}
