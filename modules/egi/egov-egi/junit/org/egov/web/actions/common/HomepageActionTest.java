package org.egov.web.actions.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.models.Favourites;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.mockito.Matchers;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.XWorkTestCase;

public class HomepageActionTest extends XWorkTestCase {
	private HomepageAction action;
	private BoundaryService boundaryManager;
	private UserService userManager;
	private ModuleDao moduleDAO;
	private PersistenceService<Favourites, Long> favouriteService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.action = new HomepageAction();
		this.boundaryManager = mock(BoundaryService.class);
		this.userManager = mock(UserService.class);
		this.moduleDAO = mock(ModuleDao.class);
		this.favouriteService = mock(PersistenceService.class);
		this.action.setBoundaryService(this.boundaryManager);
		this.action.setFavouriteService(this.favouriteService);
		this.action.setUserService(this.userManager);
		this.action.setModuleDAO(this.moduleDAO);
		this.action.setBaseURL("egi");
		this.action.setFavouriteName("favouriteName");
		this.action.setParentId(1);
		this.action.setActionId(1);
		this.action.getModel();
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.session = mock(HttpSession.class);
		when(this.request.getSession()).thenReturn(this.session);
		when(this.request.getRequestURL()).thenReturn(
				new StringBuffer("http://localhost:8080/egi/common"));
		when(this.request.getContextPath()).thenReturn("/egi");
		when(this.response.getWriter()).thenReturn(new PrintWriter(System.out));
		ServletActionContext.setRequest(this.request);
		ServletActionContext.setResponse(this.response);
		EGOVThreadLocals.setUserId("1");
	}

	public void testExecute() {
		// Test execute method
		this.action.execute();
		assertTrue(true);
	}

	public void testRedirectHomePage() throws Exception {

		// Test redirectHomepage without session and expect an exception and
		// return ERROR
		String result = this.action.execute();
		assertEquals(Action.ERROR, result);

		// Test with Boundary value null and expect an EgovRuntimeException and
		// return ERROR
		when(this.session.getAttribute("org.egov.topBndryID")).thenReturn("1");
		when(this.session.getAttribute("cityname")).thenReturn("city");
		when(this.session.getAttribute("citylogo")).thenReturn("citylogo");
		when(this.boundaryManager.getBoundary(1)).thenReturn(null);
		result = this.action.execute();
		assertEquals(Action.ERROR, result);

		// Test with CityWebSite value null and expect an EgovRuntimeException
		// and return ERROR
		final Boundary boundary = new BoundaryImpl();
		boundary.setLat(23.90f);
		boundary.setLng(23.90f);
		boundary.setName("boundryName");
		when(this.boundaryManager.getBoundary(1)).thenReturn(boundary);

		when(this.session.getAttribute("cityurl")).thenReturn("localhost");
		when(this.session.getAttribute("cityname")).thenReturn("city");
		when(this.session.getAttribute("citylogo")).thenReturn("citylogo");
		result = this.action.execute();
		assertEquals(Action.ERROR, result);

		// Test with User value null and expect an EgovRuntimeException and
		// return ERROR
		final CityWebsite cityWebsite = new CityWebsiteImpl();
		cityWebsite.setCityName("City Name");
		cityWebsite.setLogo("LOGO");
		when(this.session.getAttribute("cityname")).thenReturn("city");
		when(this.session.getAttribute("citylogo")).thenReturn("citylogo");

		when(this.userManager.getUserByID(1)).thenReturn(null);
		result = this.action.execute();
		assertEquals(Action.ERROR, result);

		// Test with all values, set UserName as null
		final User user = mock(User.class);
		user.setId(1);
		user.setUserName(null);
		final Set<Role> roles = new HashSet<Role>();
		when(user.getRoles()).thenReturn(roles);
		when(this.userManager.getUserByID(1)).thenReturn(user);
		final List<Module> moduleBeanList = new ArrayList<Module>();
		final Module module = new Module();
		module.setId(1);
		module.setModuleName("EmployeeSelfService");
		moduleBeanList.add(module);
		when(this.moduleDAO.getModuleInfoForRoleIds(Matchers.anySet()))
				.thenReturn(moduleBeanList);
		when(this.moduleDAO.getUserFavourites(1)).thenReturn(moduleBeanList);

		// Create temp Manifest file to test Product Manifest file is reading
		// for creating product specification
		final File dir = new File("TESTMETA-INF");
		dir.mkdir();
		dir.deleteOnExit();
		final File f = new File("TESTMETA-INF/TESTMANIFEST.MF");
		f.createNewFile();
		f.deleteOnExit();
		FileWriter fos = new FileWriter(f, false);
		fos.write("\r\nName: Main\r\nVersion: 1.5.4\r\nBuild-Number: sdfd");
		fos.close();
		InputStream fr = new FileInputStream(f);
		final ServletContext servletContext = mock(ServletContext.class);
		when(servletContext.getResourceAsStream(Matchers.anyString()))
				.thenReturn(fr);
		ServletActionContext.setServletContext(servletContext);
		result = this.action.execute();
		assertEquals(Action.SUCCESS, result);

		fr.close();
		fos = new FileWriter(f, false);
		fos.write("\r\nName: Main\r\nBuild-Number: sdfd\r\nVersion: 1.5.4");
		fos.close();
		fr = new FileInputStream(f);
		when(servletContext.getResourceAsStream(Matchers.anyString()))
				.thenReturn(fr);
		result = this.action.execute();
		assertEquals(Action.SUCCESS, result);
		fr.close();

		// Test with User Name
		when(user.getUserName()).thenReturn("User Name");
		result = this.action.execute();
		assertEquals(Action.SUCCESS, result);

	}

	public void testDeleteFavourites() throws Exception {
		final Favourites favourites = new Favourites();
		// Test removeFromFavourite with existing item
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(
				favourites);
		this.action.deleteFromFavourites();

		// Test removeFromFavourite with non-existing item
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(null);
		when(this.favouriteService.create(Matchers.any(Favourites.class)))
				.thenReturn(null);
		this.action.deleteFromFavourites();
	}

	public void testAddFavouraites() throws Exception {
		// Test addToFavourites with for existing item
		final Favourites favourites = new Favourites();
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(
				favourites);
		this.action.addToFavourites();

		// Test addToFavourites with for non-existing item
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(null);
		when(this.favouriteService.create(Matchers.any(Favourites.class)))
				.thenReturn(null);
		this.action.addToFavourites();

	}

	public void testGetAllModules() throws Exception {
		final Favourites favourites = new Favourites();
		final List<Module> moduleBeanList = new ArrayList<Module>();
		// Test getAllModules with empty ModuleList
		this.action.setParentId(0);
		when(this.moduleDAO.getApplicationModuleByParentId(0, 1)).thenReturn(
				moduleBeanList);
		this.action.getAllModules();

		// Test getAllModules with List of Modules with enabled true;
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(null);
		Module module = new Module();
		module.setId(1);
		module.setBaseUrl("egi");
		module.setModuleName("moduleName");
		module.setIsEnabled(true);
		moduleBeanList.add(module);
		this.action.getAllModules();

		// Test getALlModules with List of Sub Modules empty
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(
				favourites);
		module = moduleBeanList.get(0);
		module.setId(1);
		module.setIsEnabled(false);
		final List<Module> moduleList = new ArrayList<Module>();
		when(this.moduleDAO.getApplicationModuleByParentId(1, 1)).thenReturn(
				moduleList);
		this.action.getAllModules();

		// Test getALlModules with List of Sub Modules
		final Module module2 = new Module();
		module2.setId(4);
		module2.setBaseUrl("egi");
		module2.setModuleName("moduleName");
		module2.setIsEnabled(true);
		moduleList.add(module2);
		when(this.moduleDAO.getApplicationModuleByParentId(1, 1)).thenReturn(
				moduleList);
		this.action.getAllModules();

		// Test getALlModules with List of Sub Modules withour favourite
		when(
				this.favouriteService.find(Matchers.anyString(),
						Matchers.anyInt(), Matchers.anyInt())).thenReturn(null);
		this.action.getAllModules();

		// Test getALlModules with List of Sub Modules
		final Module module3 = new Module();
		module3.setId(3);
		module3.setBaseUrl("egi");
		module3.setModuleName("moduleName");
		module3.setIsEnabled(false);
		moduleList.add(module3);
		when(this.moduleDAO.getApplicationModuleByParentId(1, 1)).thenReturn(
				moduleList);
		this.action.getAllModules();
	}

}
