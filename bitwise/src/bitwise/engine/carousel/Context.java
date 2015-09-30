package bitwise.engine.carousel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import bitwise.engine.carousel.events.RequestAbortedEvent;
import bitwise.engine.carousel.events.RequestFinishedEvent;
import bitwise.engine.carousel.events.RequestPreemptedEvent;
import bitwise.engine.carousel.events.RequestRunningEvent;
import bitwise.engine.carousel.exceptions.ContextPreemptedException;
import bitwise.engine.eventbus.EventBus;
import bitwise.engine.supervisor.Supervisor;
import bitwise.model.entity.Entity;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.EntityID;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;

public final class Context implements Runnable {
	private final ContextID id;
	private final Carousel carousel;
	private final Request request;
	private final HashMap<EntityID, EntityCanister<?>> canisters = new HashMap<>();
	private final HashMap<EntityID, EntityData> entities = new HashMap<>();
	private final HashSet<EntityID> locked = new HashSet<>();
	private final ArrayList<Context> blocking = new ArrayList<>();
	private boolean enabled = true;
	private Context preemptedBy = null;
	
	protected Context(Carousel in_carousel, Request in_request) {
		id = new ContextID();
		carousel = in_carousel;
		request = in_request;
		assert(null != carousel);
		assert(null != request);
	}
	
	private void reset() {
		canisters.clear();
		entities.clear();
		locked.clear();
		enabled = true;
		preemptedBy = null;
	}
	
	private void addBlocking(Context ctx) {
		blocking.add(ctx);
	}
	
	protected Collection<Context> getBlocking() {
		return blocking;
	}
	
	public ContextID getID() {
		return id;
	}
	
	protected Collection<EntityCanister<?>> getCanisters() {
		return canisters.values();
	}
	
	protected Collection<EntityData> getEntities() {
		return entities.values();
	}
	
	protected Set<EntityID> getLocked() {
		return locked;
	}
	
	protected synchronized void disable() {
		enabled = false;
	}
	
	public synchronized boolean isEnabled() {
		return enabled;
	}
	
	protected synchronized void preempt(Context elder) {
		preemptedBy = elder;
		carousel.unlockFromContext(this);
	}
	
	public synchronized boolean isPreempted() {
		return (null != preemptedBy);
	}
	
	public synchronized void bailIfWastingTime() throws ContextPreemptedException {
		if (isPreempted())
			throw new ContextPreemptedException(this, preemptedBy);
	}
	
	protected synchronized <I extends EntityID, E extends Entity, M extends EntityMutable, D extends EntityData, C extends EntityCanister<?>> M getNew(CastEnsemble<I, E, M, D, C> ensemble) throws ContextPreemptedException {
		assert(isEnabled());
		bailIfWastingTime();
		assert(null != ensemble);
		D data = ensemble.newData();
		entities.put(data.getID(), data);
		return ensemble.mutableData(data);
	}
	
	protected synchronized <I extends EntityID, E extends Entity, M extends EntityMutable, D extends EntityData, C extends EntityCanister<?>> E getReadonly(CastEnsemble<I, E, M, D, C> ensemble, I id) throws ContextPreemptedException {
		assert(isEnabled());
		bailIfWastingTime();
		assert(null != ensemble);
		assert(null != id);
		// Case 1/3: The requested item is held in a canister.
		{
			EntityCanister<?> canister = canisters.get(id);
			if (null != canister) {
				C promotedCanister = ensemble.promoteCanister(canister);
				assert(null != promotedCanister);
				return ensemble.readonlyCanister(promotedCanister);
			}
		}
		// Case 2/3: The requested item was created earlier in this context.
		{
			EntityData data = entities.get(id);
			if (null != data) {
				D promotedData = ensemble.promoteData(data);
				assert(null != promotedData);
				return ensemble.readonlyData(promotedData);
			}
		}
		// Case 3/3: The requested item resides in the carousel.
		{
			EntityData data = carousel.getID(this, id);
			bailIfWastingTime();
			assert(null != data);
			D promotedData = ensemble.promoteData(data);
			assert(null != promotedData);
			locked.add(id);
			return ensemble.readonlyData(promotedData);
		}
	}
	
	protected synchronized <I extends EntityID, E extends Entity, M extends EntityMutable, D extends EntityData, C extends EntityCanister<?>> M getMutable(CastEnsemble<I, E, M, D, C> ensemble, I id) throws ContextPreemptedException {
		assert(isEnabled());
		bailIfWastingTime();
		assert(null != ensemble);
		assert(null != id);
		// Case 1/3: The requested item is held in a canister.
		{
			EntityCanister<?> canister = canisters.get(id);
			if (null != canister) {
				C promotedCanister = ensemble.promoteCanister(canister);
				assert(null != promotedCanister);
				return ensemble.mutableCanister(promotedCanister);
			}
		}
		// Case 2/3: The requested item was created earlier in this context.
		{
			EntityData data = entities.get(id);
			if (null != data) {
				D promotedData = ensemble.promoteData(data);
				assert(null != promotedData);
				return ensemble.mutableData(promotedData);
			}
		}
		// Case 3/3: The requested item resides in the carousel.
		{
			EntityData data = carousel.getID(this, id);
			bailIfWastingTime();
			D promotedData = ensemble.promoteData(data);
			assert(null != promotedData);
			C promotedCanister = ensemble.newCanister(promotedData);
			assert(null != promotedCanister);
			canisters.put(id, promotedCanister);
			locked.add(id);
			return ensemble.mutableCanister(promotedCanister);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("CTX<%08x>", id.getValue());
	}

	@Override
	public void run() {
		assert(isEnabled());
		assert(!isPreempted());
		final EventBus eventBus = Supervisor.getEventBus();
		try {
			eventBus.publishEventToBus(new RequestRunningEvent(request));
			boolean shouldPublish = false;
			try {
				shouldPublish = request.handle(this);
			}
			catch (ContextPreemptedException e) {
				// Nothing to do here
			}
			synchronized(carousel) {
				synchronized(this) {
					disable();
					if (isPreempted()) {
						Context elder = preemptedBy;
						eventBus.publishEventToBus(new RequestPreemptedEvent(request, elder.request));
						// Reset our state
						reset();
						// Reschedule us, depending on the state of
						// preemptedBy.
						synchronized(elder) {
							if (elder.isEnabled()) {
								// The elder context is still running.
								// Schedule the current request to re-run
								// when it finishes.
								elder.addBlocking(this);
							}
							else {
								// The elder context has finished running.
								// Re-run the current request.
								carousel.reenqueueRequest(this);
							}
						}
					}
					else {
						if (shouldPublish)
							carousel.publishChanges(this);
						carousel.unlockFromContext(this);
						
						eventBus.publishEventToBus(new RequestFinishedEvent(request, shouldPublish));
						
						Iterator<Context> itWaiting = blocking.iterator();
						while (itWaiting.hasNext())
							carousel.reenqueueRequest(itWaiting.next());
					}
				}
			}
		} catch (InterruptedException e) {
			eventBus.publishEventToBus(new RequestAbortedEvent(request));
		}
	}
}
