package org.egov.infstr.workflow.inbox;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.Action;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class InboxServiceTest {
	PersistenceService<StateAware, Long> persistenceService;
	PersistenceService<WorkflowTypes, Long> wfTypesService;
	InboxService inboxSvc;
	EISServeable eisService;
	WorkflowTypeService<StateAware> defaultService;

	@Before
	public void setupInboxService() {
		final List<WorkflowTypes> wftypes = new ArrayList<WorkflowTypes>();
		final WorkflowTypes wftype = new WorkflowTypes();
		wftype.setType("StateAware");
		wftype.setRenderYN('Y');
		wftype.setFullyQualifiedName("org.egov.infstr.models.StateAware");
		wftypes.add(wftype);
		final Position postion = new Position();
		postion.setId(1);
		postion.setName("Postion Name");
		final List<Position> positions = new ArrayList<Position>();
		positions.add(postion);

		final User user = new UserImpl();
		user.setId(1);
		user.setFirstName("first Name");
		user.setUserName("User Name");
		user.setLastName("last Name");

		this.persistenceService = createMock(PersistenceService.class);
		this.eisService = Mockito.mock(EISServeable.class);
		Mockito.when(
				this.eisService.getPositionsForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(positions);
		Mockito.when(
				this.eisService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(user);
		Mockito.when(
				this.eisService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(postion);

		this.persistenceService.setType(StateAware.class);
		replay(this.persistenceService);

		this.wfTypesService = createMock(PersistenceService.class);
		this.wfTypesService.setType(WorkflowTypes.class);
		replay(this.wfTypesService);
		reset(this.wfTypesService);
		this.inboxSvc = new InboxService();
		this.inboxSvc.setStatePersistenceService(this.persistenceService);

		expect(this.wfTypesService.findAll()).andReturn(wftypes);
		replay(this.wfTypesService);
		this.inboxSvc.setWorkflowTypeService(this.wfTypesService);
		this.inboxSvc.setEisService(this.eisService);

	}

	private void addDefaultService() {
		final Map<String, WorkflowTypeService> wfMap = new HashMap<String, WorkflowTypeService>();
		this.defaultService = createMock(DefaultWorkflowTypeService.class);
		wfMap.put("defaultWorkflowTypeService", this.defaultService);
		this.inboxSvc.setWorkflowTypeServicesMap(wfMap);
	}

	private void addNoService() {
		final Map<String, WorkflowTypeService> wfMap = new HashMap<String, WorkflowTypeService>();
		this.inboxSvc.setWorkflowTypeServicesMap(wfMap);
	}

	@Test
	public void testGetAssociatedServiceWithOnlyDefault() {
		addDefaultService();
		final WorkflowTypeService wfTypeService = this.inboxSvc
				.getAssociatedService("StateAware");
		assertEquals(wfTypeService, this.defaultService);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testGetAssociatedServiceWithNoMap() {
		addNoService();
		this.inboxSvc.getAssociatedService("StateAware");
	}

	@Test
	public void testGetWorkflowType() {
		assertNotNull(this.inboxSvc.getWorkflowType("StateAware"));
	}

	@Test
	public void testGetAssignedWorkflowTypes() {
		this.persistenceService = Mockito.mock(PersistenceService.class);
		Mockito.when(
				this.persistenceService.findAllByNamedQuery(
						Matchers.anyString(), Matchers.anyInt())).thenReturn(
				new ArrayList<StateAware>());
		this.inboxSvc.setStatePersistenceService(this.persistenceService);
		assertNotNull(this.inboxSvc.getAssignedWorkflowTypes(1));
	}

	@Test
	public void testGetAllWorkflowItems() throws Exception {
		createRenderService();
		assertNotNull(this.inboxSvc.getWorkflowItems("dummy", "1"));
	}

	@Test
	public void testBuildSenderName() {
		String value = this.inboxSvc.buildSenderName(null, null);
		assertEquals(InboxService.UNKNOWN + " / " + InboxService.UNKNOWN, value);
		final Position position = new Position();
		position.setName("position");
		final UserImpl user = new UserImpl();
		user.setFirstName("firstname");
		value = this.inboxSvc.buildSenderName(position, user);
		assertEquals("position / firstname ", value);
		user.setLastName("lastname");
		value = this.inboxSvc.buildSenderName(position, user);
		assertEquals("position / firstname lastname", value);

	}

	@Test
	public void testGetPositionForUser() {
		Mockito.when(
				this.eisService.getPositionsForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(
				new ArrayList<Position>());
		assertNotNull(this.inboxSvc.getPositionForUser(1, new Date()));
	}

	@Test
	public void testGetPrimaryPositionForUser() {
		Mockito.when(
				this.eisService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(new Position());
		assertNotNull(this.inboxSvc.getPrimaryPositionForUser(1, new Date()));
	}

	@Test
	public void testGetUserForPosition() {
		Mockito.when(
				this.eisService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(new UserImpl());
		assertNotNull(this.inboxSvc.getUserForPosition(1, new Date()));
	}

	@Test
	public void testGetStateById() {
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.inboxSvc.setStatePersistenceService(persistenceService);
		Mockito.when(
				persistenceService.findById(Matchers.anyLong(),
						Matchers.anyBoolean()))
				.thenReturn(createState("dummy"));
		assertNotNull(this.inboxSvc.getStateById(1l));
	}

	@Test
	public void testGetStateUserPosition() {
		final State state = createState("dummy");
		Mockito.when(
				this.eisService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(new Position());
		assertNotNull(this.inboxSvc.getStateUserPosition(state));
		final State state2 = createState("dummy2");
		state.setPrevious(state2);
		assertNotNull(this.inboxSvc.getStateUserPosition(state));

	}

	@Test
	public void testGetStateUser() {
		Mockito.when(
				this.eisService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(new UserImpl());
		final Position position = new Position();
		position.setId(1);
		// fetch from DB
		assertNotNull(this.inboxSvc.getStateUser(createState("dumy"), position));
		// fetch from cache
		assertNotNull(this.inboxSvc.getStateUser(createState("dumy"), position));
	}

	@Test
	public void testGetWorkflowItems() throws Exception {
		createRenderService();
		assertNotNull(this.inboxSvc.getWorkflowItems(1, 1, null));

	}

	@Test
	public void testGetNextAction() {
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.inboxSvc.setStatePersistenceService(persistenceService);
		final State state = createState("dummy");
		state.setNextAction("nextAction");
		state.setType("type");
		final Action action = new Action("name", "type", "description");
		final Action action2 = new Action("name", "type", null);
		Mockito.when(
				persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(null, action, action2);
		// With no Action
		assertNotNull(this.inboxSvc.getNextAction(state));
		// With Action
		assertNotNull(this.inboxSvc.getNextAction(state));
		// With Action but no description
		assertNotNull(this.inboxSvc.getNextAction(state));
	}

	@Test
	public void testGetWorkflowItemsByCriteria() throws Exception {
		final HashMap<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("wfType", "dummy");
		createRenderService();
		assertNotNull(this.inboxSvc.getWorkflowItems(criteria));
	}

	@Test
	public void testGetFilteredInboxItems() throws Exception {
		createRenderService();
		// without taskName
		assertNotNull(this.inboxSvc.getFilteredInboxItems(1, 1, 1, "", null,
				null));
		// with taskName
		assertNotNull(this.inboxSvc.getFilteredInboxItems(1, 1, 1, "task",
				null, null));
	}

	@Test
	public void testGetDraftItems() throws Exception {
		createRenderService();
		assertNotNull(this.inboxSvc.getDraftItems(1, 1, null));
	}

	@Test
	public void testGetAssociatedServiceError() throws Exception {
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.inboxSvc.setStatePersistenceService(persistenceService);
		final List<WorkflowTypes> workflowTypesList = new ArrayList<WorkflowTypes>();
		WorkflowTypes workflowTypes = new WorkflowTypes();
		workflowTypes.setRenderYN('Y');
		workflowTypes.setType("dummy");
		workflowTypesList.add(workflowTypes);
		Mockito.when(persistenceService.findAll())
				.thenReturn(workflowTypesList);
		this.inboxSvc.setWorkflowTypeService(persistenceService);
		this.inboxSvc.initWorkflowTypes();
		final HashMap<String, WorkflowTypeService> renderServices = new HashMap<String, WorkflowTypeService>();
		renderServices.put("defaultWorkflowTypeService",
				new DefaultWorkflowTypeService<StateAware>(persistenceService));
		final Field hashmap = this.inboxSvc.getClass().getDeclaredField(
				"wfTypeServices");
		hashmap.setAccessible(true);
		hashmap.set(this.inboxSvc, renderServices);
		try {
			this.inboxSvc.getAssociatedService("dummy");
		} catch (final Exception e) {
			assertTrue(true);
		}
		try {
			workflowTypes = workflowTypesList.get(0);
			workflowTypes.setFullyQualifiedName("org.egov.noclass.ABC");
			this.inboxSvc.getAssociatedService("dummy");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	private void createRenderService() throws NoSuchFieldException,
			IllegalAccessException {
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.inboxSvc.setStatePersistenceService(persistenceService);
		final List<String> values = new ArrayList<String>();
		values.add("dummy");
		Mockito.when(
				persistenceService.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyInt())).thenReturn(values);
		final Map<String, WorkflowTypeService> renderServices = new HashMap<String, WorkflowTypeService>();
		final WorkflowTypeService<StateAware> render = new DefaultWorkflowTypeService<StateAware>(
				persistenceService) {
			@Override
			public List<StateAware> getAssignedWorkflowItems(
					final Integer owner, final Integer userId,
					final String order) {
				return new ArrayList<StateAware>();
			}

			@Override
			public List<StateAware> getWorkflowItems(
					final Map<String, Object> criteria) {
				return new ArrayList<StateAware>();
			}

			@Override
			public List<StateAware> getFilteredWorkflowItems(
					final Integer owner, final Integer userId,
					final Integer sender, final Date fromDate, final Date toDate) {
				return new ArrayList<StateAware>();
			}

			@Override
			public List<StateAware> getDraftWorkflowItems(final Integer owner,
					final Integer userId, final String order) {
				return new ArrayList<StateAware>();
			}

			@Override
			public List<StateAware> getWorkflowItems(final String myLinkId) {
				return new ArrayList<StateAware>();
			}
		};
		renderServices.put("dummyWorkflowTypeService", render);
		final Field hashmap = this.inboxSvc.getClass().getDeclaredField(
				"wfTypeServices");
		hashmap.setAccessible(true);
		hashmap.set(this.inboxSvc, renderServices);
		final List<WorkflowTypes> workflowTypesList = new ArrayList<WorkflowTypes>();
		final WorkflowTypes workflowTypes = new WorkflowTypes();
		workflowTypes.setRenderYN('Y');
		workflowTypes.setType("dummy");
		workflowTypesList.add(workflowTypes);
		Mockito.when(persistenceService.findAll())
				.thenReturn(workflowTypesList);
		Mockito.when(
				persistenceService.find(Matchers.anyString(),
						Matchers.anyString())).thenReturn(workflowTypes);
		this.inboxSvc.setWorkflowTypeService(persistenceService);
		this.inboxSvc.initWorkflowTypes();
	}

	private State createState(final String value) {
		final State state = new State("type", value, "nextAction",
				new Position(), "comment");
		state.setCreatedBy(new UserImpl());
		state.setCreatedDate(new Date());
		return state;
	}

}
