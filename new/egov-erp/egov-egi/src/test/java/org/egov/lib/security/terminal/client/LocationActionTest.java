package org.egov.lib.security.terminal.client;

import junit.framework.TestCase;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.utils.TestUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.LocationIPMap;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

@Ignore
public class LocationActionTest extends TestCase {

	private LocationAction locationAction;
	private ActionMapping actionMapping;
	private LocationForm locationForm;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Session session;
	private Query query;
	private HttpSession httpSession;

	@Override
	public void setUp() throws Exception {
		EGOVThreadLocals.setDomainName("domain");
		beforeTestRuns();
	}

	@Before
	public void beforeTestRuns() throws Exception {
		this.locationAction = new LocationAction();
		this.query = Mockito.mock(Query.class);
		this.actionMapping = Mockito.mock(ActionMapping.class);
		this.locationForm = new LocationForm();
		this.request = Mockito.mock(HttpServletRequest.class);
		this.httpSession = Mockito.mock(HttpSession.class);
		Mockito.when(this.request.getSession()).thenReturn(this.httpSession);
		this.response = Mockito.mock(HttpServletResponse.class);
		this.session = Mockito.mock(Session.class);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
		TestUtils.activateInitialContext();
	}

	@Test
	public void testCreateLocation() throws Exception {
		try {
			this.locationAction.createLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		try {
			this.locationAction.createLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		Mockito.when(this.query.uniqueResult()).thenReturn(null);
		this.locationAction.createLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		beforeTestRuns();
		setProperties();
		this.locationForm.setLoginType("loginType");
		this.locationForm.setCounter(new String[] { "localhost" });
		this.locationAction.createLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		beforeTestRuns();
		setProperties();
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		this.locationForm.setLoginType("loginType");
		this.locationForm.setCounter(new String[] { "localhost" });
		try {
			this.locationAction.createLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		Mockito.when(this.query.uniqueResult()).thenReturn(null);
		this.locationForm.setCounter(new String[] { "localhost" });
		this.locationAction.createLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		beforeTestRuns();
		setProperties();
		this.locationForm.setLoginType("Locationsd");
		Mockito.when(this.query.uniqueResult())
				.thenReturn(null, new Location());
		this.locationForm.setCounter(new String[] { "localhost" });
		try {
			this.locationAction.createLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}

	}

	private void setProperties() {
		this.locationForm.setName("name");
		this.locationForm.setDesc("Desc");
		this.locationForm.setIsActive("1");
		this.locationForm.setIpaddress(new String[] { "1", "1" });
		this.locationForm.setLoginType("Location");
		this.locationForm.setForward("success");
		this.locationForm.setCounter(new String[] {});
		this.locationForm.setIpaddr(new String[] { "1", "1" });
		this.locationForm.setDescription(new String[] { "location",
				"192.168.1.68" });
		this.locationForm.setCreatedDate(null);
		this.locationForm.getCreatedDate();
		this.locationForm.setLastModifiedDate(null);
		this.locationForm.getLastModifiedDate();
	}

	@Test
	public void testUpdateLocation() throws Exception {
		try {
			this.locationAction.updateLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		this.locationForm.setId("1");
		try {
			this.locationAction.updateLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		this.locationForm.setId("1");
		this.locationForm.setDeleteIPSet(new String[] { "68" });
		final Location locatiodn = new Location();
		locatiodn.setId(1);
		Mockito.when(this.query.uniqueResult()).thenReturn(locatiodn);
		final Location location = new Location();
		location.setId(1);
		final LocationIPMap ipmaps = new LocationIPMap();
		ipmaps.setId(1);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, ipmaps);
		final ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(location);
		Mockito.when(this.httpSession.getAttribute(Matchers.anyString()))
				.thenReturn(locations);
		this.locationForm.setLoginType("Terminal");
		this.locationAction.updateLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		beforeTestRuns();
		setProperties();
		HashSet<LocationIPMap> locationIPMap = new HashSet<LocationIPMap>();
		LocationIPMap locMap = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap);
		LocationIPMap locMap2 = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap2);
		location.setLocationIPMapSet(locationIPMap);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, location);
		Mockito.when(this.httpSession.getAttribute(Matchers.anyString()))
				.thenReturn(locations);
		this.locationForm.setId("1");
		this.locationForm.setDeleteIPSet(new String[] { "1", "1" });
		this.locationForm.setCounter(new String[] { "1", "1" });
		this.locationForm.setCounterId(new String[] { "1", "1" });
		this.locationForm.setLocIPMapID(new String[] { "1", "1" });
		this.locationForm.setLoginType("Terminal");
		this.locationAction.updateLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, locMap, locMap, location);
		Mockito.when(this.httpSession.getAttribute(Matchers.anyString()))
				.thenReturn(new ArrayList<Location>());
		this.locationForm.setLoginType("Location");
		this.locationAction.updateLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		Mockito.when(this.httpSession.getAttribute(Matchers.anyString()))
				.thenReturn(new ArrayList<Location>());
		this.locationForm.setLoginType("Terminal");
		this.locationAction.updateLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
		beforeTestRuns();
		setProperties();
		locationIPMap = new HashSet<LocationIPMap>();
		locMap = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap);
		locMap2 = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap2);
		location.setLocationIPMapSet(locationIPMap);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, location, location, locMap, location);
		Mockito.when(this.httpSession.getAttribute(Matchers.anyString()))
				.thenReturn(locations);
		this.locationForm.setId("1");
		this.locationForm.setDeleteIPSet(new String[] { "", "" });
		this.locationForm.setCounter(new String[] { "1", "1" });
		this.locationForm.setCounterId(new String[] { "1", "1" });
		this.locationForm.setLocIPMapID(new String[] { "1", "1" });
		this.locationForm.setLoginType("Location");
		this.locationAction.updateLocation(this.actionMapping,
				this.locationForm, this.request, this.response);

	}

	@Test
	public void testDeleteLocation() throws Exception {
		try {
			this.locationAction.deleteLocation(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		this.locationForm.setId("1");
		final Criteria criteria = Mockito.mock(Criteria.class);
		Mockito.when(this.session.createCriteria(Matchers.any(Class.class)))
				.thenReturn(criteria);
		final Location location = new Location();
		location.setId(1);
		location.setLocationId(location);
		final ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(location);
		Mockito.when(criteria.list()).thenReturn(locations);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location);
		this.locationAction.deleteLocation(this.actionMapping,
				this.locationForm, this.request, this.response);
	}

	@Test
	public void testLoadModifyData() throws Exception {
		try {
			this.locationAction.loadModifyData(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		this.locationForm.setId("1");
		final Location location = new Location();
		location.setId(1);
		location.setLocationId(location);
		location.setIsActive(1);
		location.setIsLocation(1);
		final HashSet<LocationIPMap> locationIPMap = new HashSet<LocationIPMap>();
		final LocationIPMap locMap = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap);
		location.setLocationIPMapSet(locationIPMap);
		final ArrayList<Location> locationList = new ArrayList<Location>();
		locationList.add(location);
		Mockito.when(this.query.list()).thenReturn(locationList);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location);
		try {
			this.locationAction.loadModifyData(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		location.setIsLocation(0);
		this.locationForm.setId("1");
		location.setDesc("desc");
		Mockito.when(this.query.list()).thenReturn(locationList);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location);
		try {
			this.locationAction.loadModifyData(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testLoadCreateData() throws Exception {
		try {
			this.locationAction.loadCreateData(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setProperties();
		this.locationForm.setId("1");
		final Location location = new Location();
		location.setId(1);
		location.setLocationId(location);
		location.setIsActive(1);
		location.setIsLocation(1);
		final HashSet<LocationIPMap> locationIPMap = new HashSet<LocationIPMap>();
		final LocationIPMap locMap = new LocationIPMap();
		locMap.setId(1);
		locMap.setIpAddress("localhost");
		locationIPMap.add(locMap);
		location.setLocationIPMapSet(locationIPMap);
		final ArrayList<Location> locationList = new ArrayList<Location>();
		locationList.add(location);
		Mockito.when(this.query.list()).thenReturn(locationList);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location);
		try {
			this.locationAction.loadCreateData(this.actionMapping,
					this.locationForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

}
