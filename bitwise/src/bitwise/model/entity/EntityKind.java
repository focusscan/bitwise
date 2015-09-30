package bitwise.model.entity;

import bitwise.engine.carousel.CastEnsemble;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;

public class EntityKind {
	public static final CastEnsemble<EntityID, Entity, EntityMutable, EntityData, EntityCanister<?>> ENSEMBLE =
			new CastEnsemble<EntityID, Entity, EntityMutable, EntityData, EntityCanister<?>>() {

				@Override
				protected EntityData promoteData(EntityData in) {
					return in;
				}

				@Override
				protected EntityCanister<?> promoteCanister(EntityCanister<?> in) {
					return in;
				}

				@Override
				protected EntityData newData() {
					return new EntityData();
				}

				@Override
				protected Entity readonlyData(EntityData in) {
					return in;
				}

				@Override
				protected EntityMutable mutableData(EntityData in) {
					return in;
				}

				@Override
				protected EntityCanister<EntityData> newCanister(EntityData in) {
					return new EntityCanister<>(in);
				}

				@Override
				protected Entity readonlyCanister(EntityCanister<?> in) {
					return in;
				}

				@Override
				protected EntityMutable mutableCanister(EntityCanister<?> in) {
					return in;
				}
	};
	
	public static final EntityKind INSTANCE = new EntityKind();
	
	private final String name;
	
	private EntityKind() {
		name = "Entity";
	}
	
	protected EntityKind(String in_name) {
		name = in_name;
		assert(null != name);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c kinds are supposed to be singletons
	}
	
	@Override
	public String toString() {
		return String.format("%sKind", name);
	}
}
