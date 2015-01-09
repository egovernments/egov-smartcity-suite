package org.egov.web.actions.workflow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.workflow.admin.WorkflowAdminService;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.XWorkTestCase;

public class WorkflowAdminActionTest extends XWorkTestCase {
	private WorkflowAdminAction wfAdminAction;
	private WorkflowAdminService workflowAdmin;
	private InboxService inboxService;
	private DepartmentDAO departmentDao;
	private EISServeable eisService;

	@Override
	@Before
	public void setUp() throws IOException, Exception {
		super.setUp();
		this.wfAdminAction = new WorkflowAdminAction();
		this.workflowAdmin = Mockito.mock(WorkflowAdminService.class);
		this.inboxService = Mockito.mock(InboxService.class);
		this.departmentDao = Mockito.mock(DepartmentDAO.class);
		this.eisService = Mockito.mock(EISServeable.class);
		this.wfAdminAction.setWorkflowAdmin(this.workflowAdmin);
		this.wfAdminAction.setInboxService(this.inboxService);
		this.wfAdminAction.setDepartmentDao(this.departmentDao);
		this.wfAdminAction.setEisService(this.eisService);

		this.wfAdminAction.setdeptId(1);
		this.wfAdminAction.setDesigId(1);
		this.wfAdminAction.setFromDate(new Date());
		this.wfAdminAction.setNewOwner(2);
		this.wfAdminAction.setOwner(1);
		this.wfAdminAction.setQuery("1");
		this.wfAdminAction.setSender(1);
		this.wfAdminAction.setStateId("1");
		this.wfAdminAction.setToDate(new Date());
		this.wfAdminAction.setWfState("wfState");
		this.wfAdminAction.setWfType("wfType");
		final HttpServletResponse response = Mockito
				.mock(HttpServletResponse.class);
		Mockito.when(response.getWriter()).thenReturn(
				new PrintWriter(System.out));
		ServletActionContext.setResponse(response);
	}

	@Test
	public void testExecute() {
		assertEquals(Action.SUCCESS, this.wfAdminAction.execute());
	}

	@Test
	public void testGetDepartmentList() {
		Mockito.when(this.departmentDao.getAllDepartments()).thenReturn(
				new ArrayList<Department>());
		assertNotNull(this.wfAdminAction.getDepartmentList());
	}

	@Test
	public void testPopulateDesignation() throws IOException {
		final List<DesignationMaster> designations = new ArrayList<DesignationMaster>();
		Mockito.when(
				this.eisService.getAllDesignationByDept(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(designations);
		this.wfAdminAction.populateDesignation();
		assertTrue(true);
		final DesignationMaster designation = Mockito
				.mock(DesignationMaster.class);
		designations.add(designation);
		this.wfAdminAction.populateDesignation();
		assertTrue(true);

	}

	@Test
	public void testPopulateUsersByDeptAndDesig() throws IOException {
		final List<User> users = new ArrayList<User>();
		Mockito.when(
				this.eisService.getUsersByDeptAndDesig(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.any(Date.class)))
				.thenReturn(users);
		this.wfAdminAction.populateUsersByDeptAndDesig();
		assertTrue(true);
		final User user = Mockito.mock(User.class);
		users.add(user);
		this.wfAdminAction.populateUsersByDeptAndDesig();
		assertTrue(true);
	}

	@Test
	public void testPopulateDocumentType() throws IOException {
		final List<WorkflowTypes> workflowTypes = new ArrayList<WorkflowTypes>();
		final WorkflowTypes wfTypes = Mockito.mock(WorkflowTypes.class);
		workflowTypes.add(wfTypes);
		Mockito.when(this.workflowAdmin.getWorkflowTypes(Matchers.anyString()))
				.thenReturn(workflowTypes);
		this.wfAdminAction.populateDocumentType();
		assertTrue(true);
	}

	@Test
	public void testPopulateUser() throws IOException {
		final List<User> users = new ArrayList<User>();
		final User user = Mockito.mock(User.class);
		users.add(user);
		Mockito.when(
				this.workflowAdmin.getAllUserByUserName(Matchers.anyString()))
				.thenReturn(users);
		this.wfAdminAction.populateUser();
		assertTrue(true);
	}

	@Test
	public void testPopulateWorkflowState() throws IOException {
		final List<String> workflowStates = new ArrayList<String>();
		final String wfState = "wfState";
		workflowStates.add(wfState);
		Mockito.when(
				this.workflowAdmin.getWorkflowStateValues(Matchers.anyString()))
				.thenReturn(workflowStates);
		this.wfAdminAction.populateWorkflowState();
		assertTrue(true);
	}

	@Test
	public void testReassignWFItem() throws IOException {
		final String status = "RE_ASSIGNED";
		Mockito.when(
				this.workflowAdmin.reassignWFItem(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyInt())).thenReturn(
				status);
		this.wfAdminAction.reassignWFItem();
		assertTrue(true);
	}

	@Test
	public void testSearchWfItems() throws IOException {
		this.wfAdminAction.setWfType("");
		this.wfAdminAction.searchWfItems();
		assertTrue(true);
		final List<StateAware> wfStates = new ArrayList<StateAware>();
		wfStates.add(createStateAware());
		Mockito.when(
				this.inboxService.getWorkflowItems(Matchers.any(Map.class)))
				.thenReturn(wfStates);
		final WorkflowTypes workflowTypes = Mockito.mock(WorkflowTypes.class);
		Mockito.when(workflowTypes.getLink()).thenReturn(":ID");
		Mockito.when(this.inboxService.getWorkflowType(Matchers.anyString()))
				.thenReturn(workflowTypes);
		final Position position = Mockito.mock(Position.class);
		Mockito.when(
				this.inboxService.getStateUserPosition(Matchers
						.any(State.class))).thenReturn(position);
		final User user = Mockito.mock(User.class);
		Mockito.when(
				this.inboxService.getStateUser(Matchers.any(State.class),
						Matchers.any(Position.class))).thenReturn(user);
		Mockito.when(
				this.inboxService.buildSenderName(Matchers.any(Position.class),
						Matchers.any(User.class))).thenReturn("User Name");
		this.wfAdminAction.setWfType("wfType");
		final StateAware statesAware = Mockito.mock(StateAware.class);
		Mockito.when(statesAware.getStateDetails()).thenReturn(null);
		Mockito.when(statesAware.myLinkId()).thenReturn("no");
		final State state = new State("StateAware", "NEW", new Position(),
				"comments");
		state.setCreatedBy(new UserImpl());
		state.setCreatedDate(new Date());
		Mockito.when(statesAware.getCurrentState()).thenReturn(state);
		wfStates.add(statesAware);
		this.wfAdminAction.searchWfItems();
		assertTrue(true);
	}

	private StateAware createStateAware() {
		final State state = new State("StateAware", "NEW", new Position(),
				"comments");
		state.setCreatedBy(new UserImpl());
		state.setCreatedDate(new Date());

		return new StateAware() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getStateDetails() {
				return "detail";
			}

			@Override
			public Long getId() {
				return 1l;
			}

			@Override
			public State getCurrentState() {
				return getState();
			}

			@Override
			public String getStateType() {
				return this.getClass().getSimpleName();
			}

			@Override
			public State getState() {
				return state;
			}

			@Override
			public String myLinkId() {
				return this.getId().toString();
			}
		};

	}
}
