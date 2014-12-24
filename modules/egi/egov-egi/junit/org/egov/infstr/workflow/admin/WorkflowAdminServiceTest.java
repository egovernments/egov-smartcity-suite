package org.egov.infstr.workflow.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class WorkflowAdminServiceTest {
	private WorkflowAdminService wfAdminService;
	private transient PersistenceService daoService;
	private transient EISServeable eisService;
	private transient UserService userManager;
	private transient InboxService inboxService; // Delegate to inbox service
	private transient SimpleWorkflowService<StateAware> workflowService;

	@Before
	public void setUp() throws Exception {
		this.wfAdminService = new WorkflowAdminService();
		this.daoService = Mockito.mock(PersistenceService.class);
		this.eisService = Mockito.mock(EISServeable.class);
		this.userManager = Mockito.mock(UserService.class);
		this.inboxService = Mockito.mock(InboxService.class);
		this.workflowService = Mockito.mock(SimpleWorkflowService.class);
		this.wfAdminService.setPersistenceService(this.daoService);
		this.wfAdminService.setEisService(this.eisService);
		this.wfAdminService.setUserService(this.userManager);
		this.wfAdminService.setInboxService(this.inboxService);
		this.wfAdminService.setWorkflowService(this.workflowService);
	}

	@Test
	public void testGetAllUserByUserName() {
		Mockito.when(
				this.userManager.getAllUserByUserNameLike(Matchers.anyString()))
				.thenReturn(new ArrayList<User>());
		assertNotNull(this.wfAdminService.getAllUserByUserName("userName"));
	}

	@Test
	public void testGetWorkflowStateValues() {
		Mockito.when(
				this.daoService.findAllBy(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString(),
						Matchers.anyString())).thenReturn(
				new ArrayList<String>());
		assertNotNull(this.wfAdminService.getWorkflowStateValues("wfType"));
	}

	@Test
	public void testGetWorkflowTypes() {
		Mockito.when(
				this.daoService.findAllByNamedQuery(Matchers.anyString(),
						Matchers.anyString())).thenReturn(
				new ArrayList<WorkflowTypes>());
		assertNotNull(this.wfAdminService.getWorkflowTypes("name"));
	}

	@Test
	public void testReassignWFItem() {
		final List<StateAware> l = new ArrayList<StateAware>();
		final StateAware stateAware = Mockito.mock(StateAware.class);
		final Position position = new Position();
		position.setId(2);
		Mockito.when(stateAware.getCurrentState()).thenReturn(
				new State("StateAware", "NEW", position, "comments"));
		Mockito.when(
				this.daoService.find(Matchers.anyString(),
						Matchers.any(Long.class))).thenReturn(stateAware);
		final Position newOwner = new Position();
		newOwner.setId(1);
		Mockito.when(
				this.eisService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(newOwner);
		Mockito.when(
				this.workflowService.transition(Matchers.any(StateAware.class),
						Matchers.any(Position.class), Matchers.anyString()))
				.thenReturn(stateAware);
		l.add(stateAware);
		Mockito.when(
				this.inboxService.getWorkflowItems(Matchers.anyString(),
						Matchers.anyString())).thenReturn(l);

		assertEquals("RE-ASSIGNED",
				this.wfAdminService.reassignWFItem("wfType", "1", 1));
		newOwner.setId(2);
		assertEquals("OWNER-SAME",
				this.wfAdminService.reassignWFItem("wfType", "1", 1));
	}
}
