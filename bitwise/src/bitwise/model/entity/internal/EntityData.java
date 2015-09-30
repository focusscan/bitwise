package bitwise.model.entity.internal;

import bitwise.model.entity.EntityKind;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.EntityID;

public class EntityData implements EntityMutable {
	private final EntityID id;
	private String name;
	
	public EntityData() {
		id = new EntityID();
		name = "";
	}
	
	protected EntityData(EntityID in_id) {
		id = in_id;
		name = "";
	}
	
	public EntityData(EntityData that) {
		id = that.id;
		name = that.name;
	}
	
	public EntityData mutableClone() {
		return new EntityData(this);
	}
	
	@Override
	public EntityKind getKind() {
		return EntityKind.INSTANCE;
	}
	
	@Override
	public EntityID getID() {
		return id;
	}
	
	public final EntityID getIDFast() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String in) {
		name = in;
	}
}
