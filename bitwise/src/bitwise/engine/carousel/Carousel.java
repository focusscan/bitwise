package bitwise.engine.carousel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bitwise.engine.carousel.events.CarouselFastGoodbyeEvent;
import bitwise.engine.carousel.events.CarouselGoodbyeEvent;
import bitwise.engine.carousel.events.CarouselHelloEvent;
import bitwise.engine.supervisor.Service;
import bitwise.engine.supervisor.Supervisor;
import bitwise.model.entity.Entity;
import bitwise.model.entity.EntityID;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;

public final class Carousel extends Service {
	public static String serviceName() {
		return "Carousel";
	}
	
	public static int threadPoolSize() {
		return 4;
	}
	
	private final HashMap<EntityID, EntityData> carousel = new HashMap<>();
	private final HashMap<EntityID, Context> locks = new HashMap<>();
	
	public Carousel() {
		super(serviceName(), threadPoolSize());
	}
	
	public synchronized Entity peekID(EntityID in) {
		return carousel.get(in);
	}
	
	public synchronized int sizeCarousel() {
		return carousel.size();
	}
	
	public synchronized int sizeLockSet() {
		return locks.size();
	}
	
	protected Map<EntityID, Context> getLocks() {
		return locks;
	}
	
	public void enqueueRequest(Request in) {
		assert(null != in);
		Context ctx = new Context(this, in);
		getExecutor().submit(ctx);
	}
	
	protected void reenqueueRequest(Context in) {
		assert(null != in);
		assert(in.isEnabled());
		getExecutor().submit(in);
	}
	
	@Override
	protected void onStart() {
		Supervisor.getEventBus().publishEventToBus(new CarouselHelloEvent(this));
	}
	
	@Override
	protected void onShutdown() {
		Supervisor.getEventBus().publishEventToBus(new CarouselGoodbyeEvent(this));
	}
	
	@Override
	protected void onShutdownNow() {
		Supervisor.getEventBus().publishEventToBus(new CarouselFastGoodbyeEvent(this));
	}
	
	protected synchronized void publishChanges(Context ctx) {
		assert(null != ctx);
		
		// Step 1/2: Add the newly created entities to the carousel.
		{
			Iterator<EntityData> itData = ctx.getEntities().iterator();
			while (itData.hasNext()) {
				EntityData entity = itData.next();
				EntityData existing = carousel.put(entity.getID(), entity);
				assert(null == existing);
			}
		}
		// Stpe 2/2: Update existing carousel entities with changes made in the transaction.
		{
			Iterator<EntityCanister<?>> itCanister = ctx.getCanisters().iterator();
			while (itCanister.hasNext()) {
				EntityCanister<?> canister = itCanister.next();
				EntityData current = canister.getCurrent();
				EntityData original = carousel.put(canister.getID(), current);
				assert(null != original);
				assert(canister.getOriginal() == original);
				assert(original.getClass().equals(current.getClass()));
			}
		}
	}
	
	protected synchronized void unlockFromContext(Context ctx) {
		assert(null != ctx);
		Set<EntityID> locked = ctx.getLocked();
		Iterator<EntityID> itLocked = locked.iterator();
		while (itLocked.hasNext()) {
			EntityID id = itLocked.next();
			boolean lockRemoved = locks.remove(id, ctx);
			assert(lockRemoved);
		}
	}
	
	protected synchronized EntityData getID(Context ctx, EntityID in) {
		assert(null != ctx);
		assert(null != in);
		EntityData data = carousel.get(in);
		assert(null != data);
		Context lockOwner = locks.get(in);
		if (null == lockOwner) {
			locks.put(in, ctx);
			return data;
		}
		else if (ctx == lockOwner) {
			return data;
		}
		else {
			if (lockOwner.getID().getValue() < ctx.getID().getValue()) {
				// ctx has been preempted by the elder lockOwner.
				// Notify ctx, return null, and hope that the caller
				// promptly throws ContextPreemptedException.
				ctx.preempt(lockOwner);
				return null;
			}
			else {
				// We must abort lockOwner, release its locks,
				// schedule its request to be serviced when ctx'
				// request is done, then resume serving ctx.
				lockOwner.preempt(ctx);
				// lockOwner should now have released its lock.
				// So now ctx gets it.
				assert(null == locks.get(in));
				locks.put(in, ctx);
				return data;
			}
		}
	}
}
