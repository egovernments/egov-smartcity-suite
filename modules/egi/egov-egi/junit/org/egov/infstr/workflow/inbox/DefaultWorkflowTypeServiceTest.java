package org.egov.infstr.workflow.inbox;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class DefaultWorkflowTypeServiceTest {

	private DefaultWorkflowTypeService<StateAware> defaultRenderService;
	private PersistenceService daoService;
	private Session session;
	private Query query;

	@Before
	public void setUp() throws Exception {
		this.daoService = Mockito.mock(PersistenceService.class);
		this.session = Mockito.mock(Session.class);
		this.query = Mockito.mock(Query.class);
		Mockito.when(this.query.list()).thenReturn(new ArrayList<StateAware>());
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		Mockito.when(this.daoService.getSession()).thenReturn(this.session);
		this.defaultRenderService = new DefaultWorkflowTypeService<StateAware>(
				this.daoService);
		this.defaultRenderService.setWorkflowType(StateAware.class);
		this.defaultRenderService.getWorkflowType();

	}

	@Test
	public void testGetAssignedWorkflowItems() {
		assertNotNull(this.defaultRenderService.getAssignedWorkflowItems(1, 1,
				null));
	}

	@Test
	public void testGetDraftWorkflowItems() {
		assertNotNull(this.defaultRenderService.getDraftWorkflowItems(1, 1,
				null));
	}

	@Test
	public void testGetFilteredWorkflowItems() {
		// Sender 0 from and to date null
		assertNotNull(this.defaultRenderService.getFilteredWorkflowItems(1, 1,
				0, null, null));

		// Only from date null
		assertNotNull(this.defaultRenderService.getFilteredWorkflowItems(1, 1,
				1, null, new Date()));

		// Only from date null
		assertNotNull(this.defaultRenderService.getFilteredWorkflowItems(1, 1,
				1, new Date(), null));

		// Only from date null
		assertNotNull(this.defaultRenderService.getFilteredWorkflowItems(1, 1,
				1, new Date(), new Date()));
	}

	@Test
	public void testGetWorkflowItems() {
		final HashMap<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("wfType", StateAware.class.getName());
		criteria.put("wfState", "someState");
		final ArrayList<Position> positions = new ArrayList<Position>();
		final Position position = new Position();
		position.setId(1);
		positions.add(position);
		criteria.put("sender", positions);
		criteria.put("owner", positions);
		// Without from and to date;
		assertNotNull(this.defaultRenderService.getWorkflowItems(criteria));

		// Without from date
		criteria.put("toDate", new Date());
		assertNotNull(this.defaultRenderService.getWorkflowItems(criteria));

		// With from and to Date
		criteria.put("fromDate", new Date());
		assertNotNull(this.defaultRenderService.getWorkflowItems(criteria));

		// Without toDate
		criteria.remove("toDate");
		assertNotNull(this.defaultRenderService.getWorkflowItems(criteria));

	}

	@Test
	public void testGetWorkflowItemsByMyLinkId() {
		assertNotNull(this.defaultRenderService.getWorkflowItems("1"));
	}
}
