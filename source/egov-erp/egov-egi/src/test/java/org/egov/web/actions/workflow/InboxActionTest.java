package org.egov.web.actions.workflow;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.inbox.InboxComparator;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.opensymphony.xwork2.XWorkTestCase;

public class InboxActionTest extends XWorkTestCase {

	private InboxService inboxService;
	private PersistenceService persistenceService;
	private final InboxAction inboxAction = new InboxAction();
	private final Position postion = new Position();
	private final User user = new UserImpl();
	private final State state = new State("StateAware", "NEW", this.postion,
			"comments");
	private final Action action = new Action("actionname", "StateAware",
			"description");
	private final WorkflowTypes wfTypes = new WorkflowTypes();
	private StateAware stateAware;
	List<StateAware> stateList = new ArrayList<StateAware>();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.inboxService = Mockito.mock(InboxService.class);
		this.inboxAction.setInboxComparator(new InboxComparator());
		EGOVThreadLocals.setUserId("1");
		this.postion.setId(1);
		this.postion.setName("Postion Name");
		final List<Position> positions = new ArrayList<Position>();
		positions.add(this.postion);
		this.user.setId(1);
		this.user.setFirstName("first Name");
		this.user.setUserName("User Name");
		this.user.setLastName("last Name");
		this.state.setCreatedBy(this.user);
		this.state.setCreatedDate(new Date());
		Mockito.when(
				this.inboxService.getPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(positions);
		this.stateAware = new StateAware() {
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
				return InboxActionTest.this.state;
			}

			@Override
			public String myLinkId() {
				return this.getId().toString();
			}
		};
		this.stateList = new ArrayList<StateAware>();
		this.stateList.add(this.stateAware);
		Mockito.when(this.inboxService.getWorkflowItems(1, 1, null))
				.thenReturn(this.stateList);
		Mockito.when(this.inboxService.getDraftItems(1, 1, null)).thenReturn(
				this.stateList);
		this.wfTypes.setDisplayName("Display name");
		this.wfTypes
				.setFullyQualifiedName(this.stateAware.getClass().getName());
		this.wfTypes.setGroupYN('N');
		this.wfTypes.setId(1L);
		this.wfTypes.setLink("myLink?:ID");
		this.wfTypes.setModule(Mockito.mock(Module.class));
		this.wfTypes.setRenderYN('Y');
		Mockito.when(this.inboxService.getWorkflowType(Matchers.anyString()))
				.thenReturn(this.wfTypes);
		Mockito.when(
				this.inboxService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.user);
		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		Mockito.when(
				this.inboxService.getStateUserPosition(Matchers
						.any(State.class))).thenReturn(this.postion);
		Mockito.when(
				this.inboxService.getStateUser(Matchers.any(State.class),
						Matchers.any(Position.class))).thenReturn(this.user);
		Mockito.when(this.inboxService.getNextAction(Matchers.any(State.class)))
				.thenReturn("");
		this.persistenceService = Mockito.mock(PersistenceService.class);
		this.action.setId(1L);
		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(this.action);
		this.inboxAction.setPersistenceService(this.persistenceService);
		final HttpServletResponse response = Mockito
				.mock(HttpServletResponse.class);
		Mockito.when(response.getWriter()).thenReturn(
				new PrintWriter(System.out));
		ServletActionContext.setResponse(response);
	}

	public void testInboxAction() throws Exception {

		// Test execute method and return error
		this.inboxAction.getInboxData();
		this.inboxAction.getInboxDraft();
		this.inboxAction.getInboxHistory();
		this.inboxAction.setInboxService(this.inboxService);
		this.inboxAction.execute();
		this.wfTypes.setGroupYN('Y');
		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		final String result = this.inboxAction.execute();
		assertEquals("success", result);

		// Test execute method and return success
		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		Mockito.when(
				this.inboxService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(null);
		Mockito.when(
				this.inboxService.getStateUserPosition(Matchers
						.any(State.class))).thenReturn(this.postion);
		this.state.setPrevious(this.state.clone());
		this.state.setNextAction("nextAction");
		this.inboxAction.execute();
		assertEquals("success", result);
		Mockito.when(
				this.inboxService.getStateUserPosition(Matchers
						.any(State.class))).thenReturn(this.postion);
		// Test execute with workflow item wuth error
		this.user.setLastName(null);
		this.stateAware = new StateAware() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getStateDetails() {
				return null;
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
				return InboxActionTest.this.state;
			}

			@Override
			public String myLinkId() {
				return this.getId().toString();
			}
		};
		this.stateList = new ArrayList<StateAware>();
		this.stateList.add(this.stateAware);
		Mockito.when(this.inboxService.getWorkflowItems(1, 1, null))
				.thenReturn(this.stateList);
		Mockito.when(this.inboxService.getDraftItems(1, 1, null)).thenReturn(
				this.stateList);
		Mockito.when(
				this.inboxService.getUserForPosition(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.user);
		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(null);
		this.inboxAction.execute();
		assertEquals("success", result);

		// Test PopulateHistory with no data
		this.user.setLastName("LastName");
		final Action act = new Action("actionname", "StateAware", null);
		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(act);
		this.inboxAction.execute();
		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(this.action);
		Mockito.when(this.inboxService.getWorkflowItems(1, 1, null))
				.thenReturn(this.stateList);
		this.inboxAction.setStateId("");
		this.inboxAction.populateHistory();

		// Test PopulateHistory with data

		Mockito.when(this.inboxService.getStateById(1L)).thenReturn(this.state);
		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		Mockito.when(this.inboxService.getNextAction(Matchers.any(State.class)))
				.thenReturn("NEXT");
		final State previousState = this.state.getPrevious();
		previousState.setCreatedBy(this.user);
		previousState.setCreatedDate(new Date());
		previousState.setNextAction("");
		this.inboxAction.setStateId("1");
		this.inboxAction.populateHistory();

		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		this.user.setLastName(null);
		previousState.setText1(null);
		previousState.setNextAction("");
		final Action action2 = new Action("somename", "StateAware", "");
		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(action2);
		this.inboxAction.populateHistory();

		Mockito.when(
				this.persistenceService.findByNamedQuery(Matchers.anyString(),
						Matchers.anyString(), Matchers.anyString()))
				.thenReturn(this.action);
		Mockito.when(
				this.inboxService.getPrimaryPositionForUser(Matchers.anyInt(),
						Matchers.any(Date.class))).thenReturn(this.postion);
		Mockito.when(this.inboxService.getNextAction(Matchers.any(State.class)))
				.thenReturn("");
		previousState.setText1("text1");
		previousState.setNextAction("Next Action");
		this.inboxAction.populateHistory();
		Mockito.when(this.inboxService.getNextAction(Matchers.any(State.class)))
				.thenReturn("NEXT");
		// Test pollDraft,pollInbox,filterInboxData
		Mockito.when(
				this.inboxService.getDraftItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyString())).thenReturn(
				null);
		this.inboxAction.pollDraft();
		Mockito.when(
				this.inboxService.getDraftItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyString())).thenReturn(
				this.stateList);
		this.inboxAction.pollDraft();
		Mockito.when(
				this.inboxService.getWorkflowItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyString())).thenReturn(
				null);
		this.inboxAction.pollInbox();
		Mockito.when(
				this.inboxService.getWorkflowItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyString())).thenReturn(
				this.stateList);
		this.inboxAction.pollInbox();
		Mockito.when(
				this.inboxService.getFilteredInboxItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyInt(),
						Matchers.anyString(), Matchers.any(Date.class),
						Matchers.any(Date.class))).thenReturn(null);
		this.inboxAction.filterInboxData();
		Mockito.when(
				this.inboxService.getFilteredInboxItems(Matchers.anyInt(),
						Matchers.anyInt(), Matchers.anyInt(),
						Matchers.anyString(), Matchers.any(Date.class),
						Matchers.any(Date.class))).thenReturn(this.stateList);
		this.inboxAction.filterInboxData();

		// test closure and untestable code
		final HttpServletRequest request = Mockito
				.mock(HttpServletRequest.class);
		final HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		session.setAttribute("com.egov.user.LoginUserName", "name");
		ServletActionContext.setRequest(request);
		this.inboxAction.getUserName();
		this.inboxAction.getModel();
		this.inboxAction.getStateId();
		this.inboxAction.setStateId("1");
		this.inboxAction.setSender(1);
		this.inboxAction.setTask("task");
		this.inboxAction.setFromDate(new Date());
		this.inboxAction.setToDate(new Date());
		this.inboxAction.getSenderList();
		this.inboxAction.getTaskList();
		Method mm = InboxAction.class.getDeclaredMethod("loadInboxData",
				List.class);
		mm.setAccessible(true);
		mm.invoke(this.inboxAction, Collections.emptyList());
		mm = InboxAction.class.getDeclaredMethod("loadInboxHistoryData",
				State.class);
		mm.setAccessible(true);
		final State obj = null;
		mm.invoke(this.inboxAction, new Object[] { obj });

	}

}
