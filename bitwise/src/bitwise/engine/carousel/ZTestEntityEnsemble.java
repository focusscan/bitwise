package bitwise.engine.carousel;

import static org.junit.Assert.*;

import org.junit.Test;

import bitwise.model.entity.Entity;
import bitwise.model.entity.EntityKind;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;

public class ZTestEntityEnsemble {

	private void testReadonlyVsMutable(Entity entity, EntityMutable mutable) {
		// Check its readonly interface
		assertNotNull(entity);
		assertTrue(entity.getKind() == EntityKind.INSTANCE);
		assertNotNull(entity.getID());
		assertNotNull(entity.getName());
		// Check its mutable interface
		assertNotNull(mutable);
		assertTrue(entity.getKind() == mutable.getKind());
		assertTrue(entity.getID() == mutable.getID());
		assertTrue(entity.getName() == mutable.getName());
		// Save-off the old name
		String oldName = entity.getName();
		// Set a new name.
		String newName = "newName";
		assertTrue(oldName != newName);
		mutable.setName(newName);
		assertTrue(entity.getName() == newName);
		assertTrue(mutable.getName() == newName);
		// Set the old name back.
		mutable.setName(oldName);
		assertTrue(entity.getName() == oldName);
		assertTrue(mutable.getName() == oldName);
	}
	
	@Test
	public void testEnsemble() {
		// First create a new data element
		EntityData data = EntityKind.ENSEMBLE.newData();
		assertNotNull(data);
		// Check that the readonly and mutable interfaces behave correctly
		{
			Entity entity = EntityKind.ENSEMBLE.readonlyData(data);
			EntityMutable mutable = EntityKind.ENSEMBLE.mutableData(data);
			testReadonlyVsMutable(entity, mutable);
		}
		
		// Now make a canister
		EntityCanister<?> canister = EntityKind.ENSEMBLE.newCanister(data);
		assertNotNull(canister);
		// Check that the readonly and mutable interfaces behave correctly
		{
			Entity entity = EntityKind.ENSEMBLE.readonlyCanister(canister);
			EntityMutable mutable = EntityKind.ENSEMBLE.mutableCanister(canister);
			testReadonlyVsMutable(entity, mutable);
			// The canister was modified by the previous operations, let's
			// see that these changes left their mark:
			assertTrue(canister.isModified());
			assertTrue(canister.getOriginal() != canister.getModified());
			assertTrue(canister.getCurrent() == canister.getModified());
		}
		
		// Now make sure we can promote these types properly:
		EntityData demotedData = data;
		EntityData promotedData = EntityKind.ENSEMBLE.promoteData(demotedData);
		assertNotNull(promotedData);
		EntityCanister<?> demotedCanister = canister;
		EntityCanister<?> promotedCanister = EntityKind.ENSEMBLE.promoteCanister(demotedCanister);
		assertNotNull(promotedCanister);
	}
}
