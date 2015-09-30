package bitwise.engine.carousel;

import static org.junit.Assert.*;

import org.junit.Test;

import bitwise.model.entity.EntityKind;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;
import bitwise.model.project.Project;
import bitwise.model.project.ProjectKind;
import bitwise.model.project.ProjectMutable;
import bitwise.model.project.internal.ProjectCanister;
import bitwise.model.project.internal.ProjectData;

public class ZTestProjectEnsemble {

	private void testReadonlyVsMutable(Project entity, ProjectMutable mutable) {
		// Check its readonly interface
		assertNotNull(entity);
		assertTrue(entity.getKind() == ProjectKind.INSTANCE);
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
		ProjectData data = ProjectKind.ENSEMBLE.newData();
		assertNotNull(data);
		// Check that the readonly and mutable interfaces behave correctly
		{
			Project entity = ProjectKind.ENSEMBLE.readonlyData(data);
			ProjectMutable mutable = ProjectKind.ENSEMBLE.mutableData(data);
			testReadonlyVsMutable(entity, mutable);
		}
		
		// Now make a canister
		ProjectCanister<?> canister = ProjectKind.ENSEMBLE.newCanister(data);
		assertNotNull(canister);
		// Check that the readonly and mutable interfaces behave correctly
		{
			Project entity = ProjectKind.ENSEMBLE.readonlyCanister(canister);
			ProjectMutable mutable = ProjectKind.ENSEMBLE.mutableCanister(canister);
			testReadonlyVsMutable(entity, mutable);
			// The canister was modified by the previous operations, let's
			// see that these changes left their mark:
			assertTrue(canister.isModified());
			assertTrue(canister.getOriginal() != canister.getModified());
			assertTrue(canister.getCurrent() == canister.getModified());
		}
		
		// Now make sure we can promote these types properly:
		EntityData demotedData = data;
		ProjectData promotedData = ProjectKind.ENSEMBLE.promoteData(demotedData);
		assertNotNull(promotedData);
		EntityCanister<?> demotedCanister = canister;
		ProjectCanister<?> promotedCanister = ProjectKind.ENSEMBLE.promoteCanister(demotedCanister);
		assertNotNull(promotedCanister);
		
		// Now make sure we cannot promote 'Entity's to 'Project's
		{
			EntityData eData = EntityKind.ENSEMBLE.newData();
			EntityCanister<?> eCanister = EntityKind.ENSEMBLE.newCanister(eData);
			
			ProjectData promotedeData = ProjectKind.ENSEMBLE.promoteData(eData);
			assertNull(promotedeData);
			ProjectCanister<?> promotedeCanister = ProjectKind.ENSEMBLE.promoteCanister(eCanister);
			assertNull(promotedeCanister);
		}
		
		// Now check to see if we can demote a Project to an Entity
		EntityCanister<?> demotedEntityCanister = EntityKind.ENSEMBLE.promoteCanister(demotedCanister);
		assertNotNull(demotedEntityCanister);
		String demotedEntityCanisterName = "Demoted project";
		demotedEntityCanister.setName(demotedEntityCanisterName);
		
		// And now promote it back to a project
		ProjectCanister<?> promotedDemotedEntityCanister = ProjectKind.ENSEMBLE.promoteCanister(demotedEntityCanister);
		assertNotNull(promotedDemotedEntityCanister);
		assertTrue(promotedDemotedEntityCanister.getName() == demotedEntityCanisterName);
	}
}
