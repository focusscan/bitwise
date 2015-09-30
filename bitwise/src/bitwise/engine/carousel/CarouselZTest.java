package bitwise.engine.carousel;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import bitwise.engine.carousel.events.CarouselGoodbyeEvent;
import bitwise.engine.carousel.events.CarouselHelloEvent;
import bitwise.engine.carousel.events.RequestFinishedEvent;
import bitwise.engine.carousel.events.RequestPreemptedEvent;
import bitwise.engine.carousel.events.RequestRunningEvent;
import bitwise.engine.carousel.exceptions.ContextPreemptedException;
import bitwise.engine.eventbus.EventBusHelloEvent;
import bitwise.engine.eventbus.EventNode;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.events.ServiceTerminatedEvent;
import bitwise.model.entity.Entity;
import bitwise.model.entity.EntityKind;
import bitwise.model.entity.EntityMutable;
import bitwise.model.entity.EntityID;
import bitwise.model.project.Project;
import bitwise.model.project.ProjectID;
import bitwise.model.project.ProjectKind;
import bitwise.model.project.ProjectMutable;
import bitwise.model.project.internal.ProjectData;

public class CarouselZTest {
	
	private EventNode help_carouselStart(EventNode eventNode) throws InterruptedException {
		// Remember how many things are in the carousel, etc
		int sizeCanister = Supervisor.getCarousel().sizeCarousel();
		int sizeLocks = Supervisor.getCarousel().sizeLockSet();
		
		// Verify that the carousel is not running
		assertFalse(Supervisor.carouselServiceIsRunning());
		
		// Start the carousel
		assertTrue(Supervisor.startCarouselService());
		
		// Verify that the event is a CarouselHello
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof CarouselHelloEvent);
		
		// Verify that the carousel is running
		assertTrue(Supervisor.carouselServiceIsRunning());
		
		// Verify that the carousel's size is unchanged, etc
		assertEquals(sizeCanister, Supervisor.getCarousel().sizeCarousel());
		assertEquals(sizeLocks, Supervisor.getCarousel().sizeLockSet());
		
		return eventNode;
	}
	
	private EventNode help_carouselStop(EventNode eventNode) throws InterruptedException {
		// Remember how many things are in the carousel, etc
		int sizeCanister = Supervisor.getCarousel().sizeCarousel();
		int sizeLocks = Supervisor.getCarousel().sizeLockSet();
		
		// Verify that the carousel is running
		assertTrue(Supervisor.carouselServiceIsRunning());
		
		// Interrupt the carousel
		assertTrue(Supervisor.stopCarouselService());
		
		// Verify that the event is a CarouselGoodbye
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof CarouselGoodbyeEvent);
		
		// Verify that the carousel has terminated
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof ServiceTerminatedEvent);
		
		// Verify the carousel is no longer running
		assertFalse(Supervisor.carouselServiceIsRunning());
		
		// Verify that the carousel's size is unchanged, etc
		assertEquals(sizeCanister, Supervisor.getCarousel().sizeCarousel());
		assertEquals(sizeLocks, Supervisor.getCarousel().sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselStartStop() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				eventNode = help_carouselStart(eventNode);
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	private EventNode help_carouselNew(EventNode eventNode, EntityID[] entityID, ProjectID[] projectID) throws InterruptedException {
		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName = new String[entityID.length];
		final String[] projectName = new String[projectID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[2];
		final int requestLatch = 0;
		final int testLatch = 1;
		latches[requestLatch] = new CountDownLatch(1);
		latches[testLatch] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[1];
		
		Request makeNewThing = new Request("testCarouselNew") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[0] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getNew(ctx);
					
					entityID[i] = entity.getID();
					assertNotNull(entityID[i]);
					entityName[i] = String.format("Entity %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName[i]);
				}
				for (int i = 0; i < projectID.length; i++) {
					ProjectMutable project = ProjectKind.ENSEMBLE.getNew(ctx);
					
					projectID[i] = project.getID();
					assertNotNull(projectID[i]);
					projectName[i] = String.format("Project %s %s", projectID[i].toString(), project.toString());
					project.setName(projectName[i]);
				}
				
				// Signal that we're ready for inspection
				latches[requestLatch].countDown();
				// Wait for inspection to complete
				latches[testLatch].await();
				
				return true;
			}
		};
		
		carousel.enqueueRequest(makeNewThing);
		
		// Verify that the event is a RequestRunning
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
		assertTrue( makeNewThing == ((RequestRunningEvent)eventNode.getEvent()).getRequest() );
		
		// Wait for the request to have acquired all its locks
		latches[requestLatch].await();
		
		// Verify the lock sets are correct
		Map<EntityID, Context> locks = carousel.getLocks();
		assertEquals(0, locks.size());
		assertEquals(locks.size(), carousel.sizeLockSet());
		assertEquals(locks.size(), leakedCtx[0].getLocked().size());
		for (int i = 0; i < entityID.length; i++)
			assertNull(locks.get(entityID[i]));
		for (int i = 0; i < projectID.length; i++)
			assertNull(locks.get(projectID[i]));
		
		// Tell the request we're satisfied with our inspection
		latches[testLatch].countDown();
		
		// Wait for request to finish, which will be indicated on the event bus
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
		assertTrue( makeNewThing == ((RequestFinishedEvent)eventNode.getEvent()).getRequest() );
		assertTrue( ((RequestFinishedEvent)eventNode.getEvent()).publishedChanged() );
		
		// Verify that the context was disabled
		assertFalse(leakedCtx[0].isEnabled());
		
		// Now peek at the things we made
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName[i] == thingWeMade.getName());
		}
		for (int i = 0; i < projectID.length; i++) {
			Entity thingWeMade = carousel.peekID(projectID[i]);
			assertTrue(projectName[i] == thingWeMade.getName());
		}
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(entityID.length + projectID.length, carousel.sizeCarousel() - originalSizeCarousel);
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselNew() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[numNewPerKind];
				eventNode = help_carouselNew(eventNode, entityID, projectID);
								
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	private EventNode help_carouselModify(boolean shouldPublish, EventNode eventNode, EntityID[] entityID, ProjectID[] projectID) throws InterruptedException {
		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName = new String[entityID.length];
		final String[] projectName = new String[projectID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[2];
		final int requestLatch = 0;
		final int testLatch = 1;
		latches[requestLatch] = new CountDownLatch(1);
		latches[testLatch] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[1];
		
		Request modifyThing = new Request("testCarouselModify") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[0] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName[i] = String.format("Modified entity %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName[i]);
				}
				for (int i = 0; i < projectID.length; i++) {
					ProjectMutable project = ProjectKind.ENSEMBLE.getMutable(ctx, projectID[i]);
					
					projectName[i] = String.format("Modified project %s %s", projectID[i].toString(), project.toString());
					project.setName(projectName[i]);
				}
				
				// Signal that we're ready for inspection
				latches[requestLatch].countDown();
				// Wait for inspection to complete
				latches[testLatch].await();
				
				return shouldPublish;
			}
		};
		
		carousel.enqueueRequest(modifyThing);
		
		// Verify that the event is a RequestRunning
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
		assertTrue( modifyThing == ((RequestRunningEvent)eventNode.getEvent()).getRequest() );
		
		// Wait for the request to have acquired all its locks
		latches[requestLatch].await();
		
		// Verify the lock sets are correct
		Map<EntityID, Context> locks = carousel.getLocks();
		assertEquals(entityID.length + projectID.length, locks.size());
		assertEquals(locks.size(), carousel.sizeLockSet());
		assertEquals(locks.size(), leakedCtx[0].getLocked().size());
		for (int i = 0; i < entityID.length; i++) {
			assertTrue(leakedCtx[0] == locks.get(entityID[i]));
			assertTrue(leakedCtx[0].getLocked().contains(entityID[i]));
		}
		for (int i = 0; i < projectID.length; i++) {
			assertTrue(leakedCtx[0] == locks.get(projectID[i]));
			assertTrue(leakedCtx[0].getLocked().contains(projectID[i]));
		}
		
		// Tell the request we're satisfied with our inspection
		latches[testLatch].countDown();
		
		// Wait for request to finish, which will be indicated on the event bus
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
		assertTrue( modifyThing == ((RequestFinishedEvent)eventNode.getEvent()).getRequest() );
		assertEquals(shouldPublish, ((RequestFinishedEvent)eventNode.getEvent()).publishedChanged() );
		
		// Verify that the context was disabled
		assertFalse(leakedCtx[0].isEnabled());
		
		// Now peek at the things we modified, if we asked them to be modified
		if (shouldPublish) {
			for (int i = 0; i < entityID.length; i++) {
				Entity thingWeMade = carousel.peekID(entityID[i]);
				assertTrue(entityName[i] == thingWeMade.getName());
			}
			for (int i = 0; i < projectID.length; i++) {
				Entity thingWeMade = carousel.peekID(projectID[i]);
				assertTrue(projectName[i] == thingWeMade.getName());
			}
		}
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(originalSizeCarousel, carousel.sizeCarousel());
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselModify() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[numNewPerKind];
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				
				// Now modify those things
				eventNode = help_carouselModify(true, eventNode, entityID, projectID);
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	@Test
	public void testCarouselModifyNoPublish() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[numNewPerKind];
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				
				// Now modify those things
				eventNode = help_carouselModify(false, eventNode, entityID, projectID);
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	@Test
	public void testCarouselModifyAsLower() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final ProjectID[] projectID = new ProjectID[4];
				eventNode = help_carouselNew(eventNode, new EntityID[0], projectID);
				
				// Peek and make sure we just made a project
				assertTrue( Supervisor.getCarousel().peekID(projectID[0]) instanceof ProjectData );
				
				// Now modify those things
				eventNode = help_carouselModify(true, eventNode, projectID, new ProjectID[0]);
				
				// Peek and make sure it's still a project
				assertTrue( Supervisor.getCarousel().peekID(projectID[0]) instanceof ProjectData );
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	private EventNode help_carouselReadonly(EventNode eventNode, EntityID[] entityID, ProjectID[] projectID) throws InterruptedException {
		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName = new String[entityID.length];
		final String[] projectName = new String[projectID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[2];
		final int requestLatch = 0;
		final int testLatch = 1;
		latches[requestLatch] = new CountDownLatch(1);
		latches[testLatch] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[1];
		
		Request modifyThing = new Request("testCarouselModify") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[0] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					Entity entity = EntityKind.ENSEMBLE.getReadonly(ctx, entityID[i]);
					entityName[i] = entity.getName();
				}
				for (int i = 0; i < projectID.length; i++) {
					Project project = ProjectKind.ENSEMBLE.getReadonly(ctx, projectID[i]);
					projectName[i] = project.getName();
				}
				
				// Signal that we're ready for inspection
				latches[requestLatch].countDown();
				// Wait for inspection to complete
				latches[testLatch].await();
				
				return true;
			}
		};
		
		carousel.enqueueRequest(modifyThing);
		
		// Verify that the event is a RequestRunning
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
		assertTrue( modifyThing == ((RequestRunningEvent)eventNode.getEvent()).getRequest() );
		
		// Wait for the request to have acquired all its locks
		latches[requestLatch].await();
		
		// Verify the lock sets are correct
		Map<EntityID, Context> locks = carousel.getLocks();
		assertEquals(entityID.length + projectID.length, locks.size());
		assertEquals(locks.size(), carousel.sizeLockSet());
		assertEquals(locks.size(), leakedCtx[0].getLocked().size());
		for (int i = 0; i < entityID.length; i++) {
			assertTrue(leakedCtx[0] == locks.get(entityID[i]));
			assertTrue(leakedCtx[0].getLocked().contains(entityID[i]));
		}
		for (int i = 0; i < projectID.length; i++) {
			assertTrue(leakedCtx[0] == locks.get(projectID[i]));
			assertTrue(leakedCtx[0].getLocked().contains(projectID[i]));
		}
		
		// Tell the request we're satisfied with our inspection
		latches[testLatch].countDown();
		
		// Wait for request to finish, which will be indicated on the event bus
		eventNode = eventNode.waitOnNext();
		System.out.println(eventNode.getEvent());
		assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
		assertTrue( modifyThing == ((RequestFinishedEvent)eventNode.getEvent()).getRequest() );
		assertTrue( ((RequestFinishedEvent)eventNode.getEvent()).publishedChanged() );
		
		// Verify that the context was disabled
		assertFalse(leakedCtx[0].isEnabled());
		
		// Now peek at the things we made
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName[i] == thingWeMade.getName());
		}
		for (int i = 0; i < projectID.length; i++) {
			Entity thingWeMade = carousel.peekID(projectID[i]);
			assertTrue(projectName[i] == thingWeMade.getName());
		}
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(originalSizeCarousel, carousel.sizeCarousel());
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselReadonly() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[numNewPerKind];
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				
				// Now try reading those things in a context
				eventNode = help_carouselReadonly(eventNode, entityID, projectID);
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	private EventNode help_carouselConcurrentModify(EventNode eventNode, EntityID[] entityID, ProjectID[] projectID) throws InterruptedException {
		if (Carousel.threadPoolSize() < 2)
			return eventNode;

		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName = new String[entityID.length];
		final String[] projectName = new String[projectID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[3];
		final int entityLatch = 0;
		final int projectLatch = 1;
		final int testLatch = 2;
		latches[entityLatch] = new CountDownLatch(1);
		latches[projectLatch] = new CountDownLatch(1);
		latches[testLatch] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[2];
		final int entityCtx = 0;
		final int projectCtx = 1;
		
		Request modifyEntity = new Request("testCarouselConcurrentModify - Entity") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[entityCtx] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName[i] = String.format("Modified entity %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName[i]);
				}
				
				// Signal that we're ready for inspection
				latches[entityLatch].countDown();
				// Wait for inspection to complete
				latches[testLatch].await();
				
				return true;
			}
		};
		
		Request modifyProject = new Request("testCarouselConcurrentModify - Project") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[projectCtx] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					ProjectMutable project = ProjectKind.ENSEMBLE.getMutable(ctx, projectID[i]);
					
					projectName[i] = String.format("Modified project %s %s", projectID[i].toString(), project.toString());
					project.setName(projectName[i]);
				}
				
				// Signal that we're ready for inspection
				latches[projectLatch].countDown();
				// Wait for inspection to complete
				latches[testLatch].await();
				
				return true;
			}
		};
		
		carousel.enqueueRequest(modifyEntity);
		carousel.enqueueRequest(modifyProject);
		
		// Verify that we get a RequestRunning for both requests
		{
			Request temp_modifyEntity = modifyEntity;
			Request temp_modifyProject = modifyProject;
			for (int i = 0; i < 2; i++) {
				eventNode = eventNode.waitOnNext();
				System.out.println(eventNode.getEvent());
				
				assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
				Request eventRequest = ((RequestRunningEvent)eventNode.getEvent()).getRequest();
				assertNotNull(eventRequest);
				assertTrue( (temp_modifyEntity == eventRequest) || (temp_modifyProject == eventRequest) );
				if (temp_modifyEntity == eventRequest)
					temp_modifyEntity = null;
				if (temp_modifyProject == eventRequest)
					temp_modifyProject = null;
			}
		}
		
		// Wait for the requests to have acquired all their locks
		latches[entityLatch].await();
		latches[projectLatch].await();
		
		// Verify the lock sets are correct
		Map<EntityID, Context> locks = carousel.getLocks();
		assertEquals(entityID.length + projectID.length, locks.size());
		assertEquals(locks.size(), carousel.sizeLockSet());
		assertEquals(entityID.length, leakedCtx[entityCtx].getLocked().size());
		assertEquals(projectID.length, leakedCtx[projectCtx].getLocked().size());
		for (int i = 0; i < entityID.length; i++) {
			assertTrue(leakedCtx[entityCtx] == locks.get(entityID[i]));
			assertTrue(leakedCtx[entityCtx].getLocked().contains(entityID[i]));
		}
		for (int i = 0; i < projectID.length; i++) {
			assertTrue(leakedCtx[projectCtx] == locks.get(projectID[i]));
			assertTrue(leakedCtx[projectCtx].getLocked().contains(projectID[i]));
		}
		
		// Tell the request we're satisfied with our inspection
		latches[testLatch].countDown();
		
		// Wait for request to finish, which will be indicated on the event bus		
		// Verify that the event is a RequestFinished, and its publish request was honored
		{
			Request temp_modifyEntity = modifyEntity;
			Request temp_modifyProject = modifyProject;
			for (int i = 0; i < 2; i++) {
				eventNode = eventNode.waitOnNext();
				System.out.println(eventNode.getEvent());
				
				assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
				Request eventRequest = ((RequestFinishedEvent)eventNode.getEvent()).getRequest();
				assertNotNull(eventRequest);
				assertTrue( ((RequestFinishedEvent)eventNode.getEvent()).publishedChanged() );
				assertTrue( (temp_modifyEntity == eventRequest) || (temp_modifyProject == eventRequest) );
				if (temp_modifyEntity == eventRequest)
					temp_modifyEntity = null;
				if (temp_modifyProject == eventRequest)
					temp_modifyProject = null;
			}
		}
		
		// Verify that the contexts were disabled
		assertFalse(leakedCtx[entityCtx].isEnabled());
		assertFalse(leakedCtx[projectCtx].isEnabled());
		
		// Now peek at the things we modified
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName[i] == thingWeMade.getName());
		}
		for (int i = 0; i < projectID.length; i++) {
			Entity thingWeMade = carousel.peekID(projectID[i]);
			assertTrue(projectName[i] == thingWeMade.getName());
		}
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(originalSizeCarousel, carousel.sizeCarousel());
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselConcurrent() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[numNewPerKind];
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				
				// Now try reading those things in a pair of concurrent contexts
				eventNode = help_carouselConcurrentModify(eventNode, entityID, projectID);
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
	
	// Two requests - modify1, modify2 -- submitted in that order.
	// Due to trickery, modify1 acquires all of its locks, then modify2
	// attemps to do the same; but as modify1 isn't finished yet, modify2
	// gets preempted on the spot. We expect:
	// modify1 finishes execution
	// modify2 gets scheduled to rerun after modify1 is finished.
	private EventNode help_carouselPreemptionModify(EventNode eventNode, EntityID[] entityID) throws InterruptedException {
		if (Carousel.threadPoolSize() < 2)
			return eventNode;

		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName1 = new String[entityID.length];
		final String[] entityName2 = new String[entityID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[3];
		final int req1locked = 0;
		final int req1mayexit = 1;
		final int req2mayexit = 2;
		latches[req1locked] = new CountDownLatch(1);
		latches[req1mayexit] = new CountDownLatch(1);
		latches[req2mayexit] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[2];
		final int req1Ctx = 0;
		final int req2Ctx = 1;
		
		Request modify1 = new Request("testCarouselPreemptionModify - 1") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[req1Ctx] = ctx;
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName1[i] = String.format("Modified entity 1 %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName1[i]);
				}
				
				// Signal that we've acquired all of our locks
				latches[req1locked].countDown();
				// Wait for permission to terminate
				latches[req1mayexit].await();
				
				return true;
			}
		};
		
		Request modify2 = new Request("testCarouselPreemptionModify - 2") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[req2Ctx] = ctx;
				// Wait for the other request to get its locks
				latches[req1locked].await();
				
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName2[i] = String.format("Modified entity 2 %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName2[i]);
				}
				
				// Wait for permission to terminate
				latches[req2mayexit].await();
				
				return true;
			}
		};
		
		carousel.enqueueRequest(modify1);
		carousel.enqueueRequest(modify2);
		
		// Verify that we get a RequestRunning for both requests
		{
			Request temp_modifyEntity = modify1;
			Request temp_modifyProject = modify2;
			for (int i = 0; i < 2; i++) {
				eventNode = eventNode.waitOnNext();
				System.out.println(eventNode.getEvent());
				
				assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
				Request eventRequest = ((RequestRunningEvent)eventNode.getEvent()).getRequest();
				assertNotNull(eventRequest);
				assertTrue( (temp_modifyEntity == eventRequest) || (temp_modifyProject == eventRequest) );
				if (temp_modifyEntity == eventRequest)
					temp_modifyEntity = null;
				if (temp_modifyProject == eventRequest)
					temp_modifyProject = null;
			}
		}
		
		// Wait for modify1 to acquire its locks
		latches[req1locked].await();
		
		// Now we expect modify2 to proceed, and immediately get
		// preempted
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestPreemptedEvent);
			RequestPreemptedEvent event = (RequestPreemptedEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
			assertTrue(modify1 == event.getPreemptedBy());
		}
		
		// Now we let modify1 finish
		latches[req1mayexit].countDown();
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
			RequestFinishedEvent event = (RequestFinishedEvent)eventNode.getEvent();
			assertTrue(modify1 == event.getRequest());
			assertTrue(event.publishedChanged());
		}
		// modify2 should have been scheduled to run when modify1 finishes
		assertTrue(leakedCtx[req1Ctx].getBlocking().contains(leakedCtx[req2Ctx]));
		
		// Now we expect modify2 to get re-run
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
			RequestRunningEvent event = (RequestRunningEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
		}
		
		// Before we let modify2 exit, check that the carousel shows
		// modify1's values:
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName1[i] == thingWeMade.getName());
		}
		// Now let modify2 exit
		latches[req2mayexit].countDown();
		
		// Then it should exit
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
			RequestFinishedEvent event = (RequestFinishedEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
			assertTrue(event.publishedChanged());
		}
		
		// Check that modify2's actions took effect:
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName2[i] == thingWeMade.getName());
		}
		
		// Verify that the contexts were disabled
		assertFalse(leakedCtx[req1Ctx].isEnabled());
		assertFalse(leakedCtx[req2Ctx].isEnabled());
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(originalSizeCarousel, carousel.sizeCarousel());
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	// Two requests - modify1 and modify2, submitted in that order.
	// But by trickery, modify2 acquires all of the locks before
	// modify1 gets around to it. We expect:
	// modify1 is allowed to blow through the locks
	// modify2 gets rescheduled to run when modify1 completes
	private EventNode help_carouselPreemptionModify2(EventNode eventNode, EntityID[] entityID) throws InterruptedException {
		if (Carousel.threadPoolSize() < 2)
			return eventNode;

		final Carousel carousel = Supervisor.getCarousel();
		// Remember how many things the carousel held before we got here
		final int originalSizeCarousel = carousel.sizeCarousel();
		
		final String[] entityName1 = new String[entityID.length];
		final String[] entityName2 = new String[entityID.length];
		
		final CountDownLatch[] latches = new CountDownLatch[4];
		final int req1locked = 0;
		final int req2locked = 1;
		final int req1mayexit = 2;
		final int req2mayexit = 3;
		latches[req1locked] = new CountDownLatch(1);
		latches[req2locked] = new CountDownLatch(1);
		latches[req1mayexit] = new CountDownLatch(1);
		latches[req2mayexit] = new CountDownLatch(1);
		final Context[] leakedCtx = new Context[2];
		final int req1Ctx = 0;
		final int req2Ctx = 1;
		
		Request modify1 = new Request("testCarouselPreemptionModify2 - 1") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[req1Ctx] = ctx;
				// Wait until modify2 gets its locks
				latches[req2locked].await();
				
				// Get our locks - should succeed, we should preempt modify2
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName1[i] = String.format("Modified 2 entity 1 %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName1[i]);
				}
				
				// Notify that we've got our locks
				latches[req1locked].countDown();
				// Wait for permission to terminate
				latches[req1mayexit].await();
				
				return true;
			}
		};
		
		Request modify2 = new Request("testCarouselPreemptionModify2 - 2") {
			@Override
			public boolean handle(Context ctx) throws ContextPreemptedException, InterruptedException {
				leakedCtx[req2Ctx] = ctx;
				// Get our locks before the elder modify1
				for (int i = 0; i < entityID.length; i++) {
					EntityMutable entity = EntityKind.ENSEMBLE.getMutable(ctx, entityID[i]);
					
					entityName2[i] = String.format("Modified 2 entity 2 %s %s", entityID[i].toString(), entity.toString());
					entity.setName(entityName2[i]);
				}
				
				// Tell modify1 that we've got our locks
				latches[req2locked].countDown();
				// Wait for permission to terminate
				latches[req2mayexit].await();
				
				return true;
			}
		};
		
		carousel.enqueueRequest(modify1);
		carousel.enqueueRequest(modify2);
		
		// Verify that we get a RequestRunning for both requests
		{
			Request temp_modifyEntity = modify1;
			Request temp_modifyProject = modify2;
			for (int i = 0; i < 2; i++) {
				eventNode = eventNode.waitOnNext();
				System.out.println(eventNode.getEvent());
				
				assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
				Request eventRequest = ((RequestRunningEvent)eventNode.getEvent()).getRequest();
				assertNotNull(eventRequest);
				assertTrue( (temp_modifyEntity == eventRequest) || (temp_modifyProject == eventRequest) );
				if (temp_modifyEntity == eventRequest)
					temp_modifyEntity = null;
				if (temp_modifyProject == eventRequest)
					temp_modifyProject = null;
			}
		}
		
		// Wait until modify2 gets its locks
		latches[req2locked].await();
		
		// Wait for modify1 to get its locks; so long as modify2
		// is still running when modify1 goes to get its first
		// lock, we should see modify2 get preempted by modify1.
		latches[req1locked].await();
		
		// By now modify2 has been preempted by modify1. Let it
		// terminate so we can inspect the result.
		latches[req2mayexit].countDown();
		
		// We should now see an announcement that modify2 was preempted by modify1
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestPreemptedEvent);
			RequestPreemptedEvent event = (RequestPreemptedEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
			assertTrue(modify1 == event.getPreemptedBy());
		}
		
		// Now we let modify1 exit
		latches[req1mayexit].countDown();
		
		// We should see the announcement that modify1 finished
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
			RequestFinishedEvent event = (RequestFinishedEvent)eventNode.getEvent();
			assertTrue(modify1 == event.getRequest());
			assertTrue(event.publishedChanged());
		}
		// modify2 should have been scheduled to run when modify1 finishes
		assertTrue(leakedCtx[req1Ctx].getBlocking().contains(leakedCtx[req2Ctx]));
		
		// Now we expect modify2 to get re-run
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestRunningEvent);
			RequestRunningEvent event = (RequestRunningEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
		}
		
		// Then modify2 should exit
		{
			eventNode = eventNode.waitOnNext();
			System.out.println(eventNode.getEvent());
			assertTrue(eventNode.getEvent() instanceof RequestFinishedEvent);
			RequestFinishedEvent event = (RequestFinishedEvent)eventNode.getEvent();
			assertTrue(modify2 == event.getRequest());
			assertTrue(event.publishedChanged());
		}
		
		// Check that modify2's actions took effect:
		for (int i = 0; i < entityID.length; i++) {
			Entity thingWeMade = carousel.peekID(entityID[i]);
			assertTrue(entityName2[i] == thingWeMade.getName());
		}
		
		// Verify that the contexts were disabled
		assertFalse(leakedCtx[req1Ctx].isEnabled());
		assertFalse(leakedCtx[req2Ctx].isEnabled());
		
		// Lastly, check the carousel's global properties to see that they
		// are correct
		assertEquals(originalSizeCarousel, carousel.sizeCarousel());
		assertEquals(0, carousel.sizeLockSet());
		
		return eventNode;
	}
	
	@Test
	public void testCarouselPreemption() {
		synchronized(Supervisor.getInstance()) {
			try {
				EventNode eventNode = Supervisor.getEventBus().getEventNode();
				assertNotNull(eventNode);
				
				// Verify that the event is either an EventBusHello or a ServiceTerminated
				assertTrue((eventNode.getEvent() instanceof EventBusHelloEvent)
						|| (eventNode.getEvent() instanceof ServiceTerminatedEvent));
				
				// Start the carousel
				eventNode = help_carouselStart(eventNode);
				
				// Make some new things and put them in the carousel
				final int numNewPerKind = 4;
				final EntityID[] entityID = new EntityID[numNewPerKind];
				final ProjectID[] projectID = new ProjectID[0];
				// Now try forcing some locking conflicts
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				eventNode = help_carouselPreemptionModify(eventNode, entityID);
				eventNode = help_carouselNew(eventNode, entityID, projectID);
				eventNode = help_carouselPreemptionModify2(eventNode, entityID);
				
				// Stop the carousel
				eventNode = help_carouselStop(eventNode);
			}
			catch (InterruptedException e) {
				assertTrue(false);
			}
		}
	}
}
