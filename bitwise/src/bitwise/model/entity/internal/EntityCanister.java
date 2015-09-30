package bitwise.model.entity.internal;

import bitwise.model.entity.EntityKind;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.EntityID;

public class EntityCanister<T extends EntityData> implements EntityMutable {
	private final T original;
	private T modified = null;
	
	public EntityCanister(T in_original) {
		original = in_original;
		assert(null != original);
	}
	
	public synchronized boolean isModified() {
		return (null != modified);
	}
	
	public T getOriginal() {
		return original;
	}
	
	public T getModified() {
		return modified;
	}
	
	public synchronized T getCurrent() {
		return isModified() ? modified : original;
	}
	
	@SuppressWarnings("unchecked")
	protected synchronized T setModified() {
		if (!isModified())
			modified = (T)(original.mutableClone());
		assert(original.getClass().equals(modified.getClass()));
		return modified;
	}
	
	@Override
	public EntityKind getKind() {
		return getOriginal().getKind();
	}
	
	@Override
	public EntityID getID() {
		return getOriginal().getID();
	}
	
	@Override
	public synchronized String getName() {
		return getCurrent().getName();
	}
	
	@Override
	public synchronized void setName(String in) {
		setModified().setName(in);
	}
}
