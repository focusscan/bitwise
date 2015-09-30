package bitwise.engine.carousel;

import bitwise.engine.carousel.exceptions.ContextPreemptedException;
import bitwise.model.entity.Entity;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.EntityID;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;

public abstract class CastEnsemble<I extends EntityID, E extends Entity, M extends EntityMutable, D extends EntityData, C extends EntityCanister<?>> {
	protected abstract D promoteData(EntityData in);
	protected abstract C promoteCanister(EntityCanister<?> in);
	
	protected abstract D newData();
	protected abstract E readonlyData(D in);
	protected abstract M mutableData(D in);
	
	protected abstract C newCanister(D in);
	protected abstract E readonlyCanister(C in);
	protected abstract M mutableCanister(C in);
	
	public final M getNew(Context ctx) throws ContextPreemptedException {
		return ctx.<I, E, M, D, C>getNew(this);
	}
	
	public final E getReadonly(Context ctx, I id) throws ContextPreemptedException {
		return ctx.<I, E, M, D, C>getReadonly(this, id);
	}
	
	public final M getMutable(Context ctx, I id) throws ContextPreemptedException {
		return ctx.<I, E, M, D, C>getMutable(this, id);
	}
}
