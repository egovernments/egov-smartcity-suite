/*
 * @(#)HomepageAction.java 3.0, 14 Jun, 2013 1:40:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.models.Favourites;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HomepageAction. ERP Portal Page Rendering
 */
@ParentPackage("egov")
public class HomepageAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(HomepageAction.class);
	private transient BoundaryService boundaryService;
	private transient UserService userService;
	private transient ModuleDao moduleDAO;
	private transient PersistenceService<Favourites, Long> favouriteService;
	private transient Integer parentId;
	private transient String baseURL;
	private transient Integer actionId;
	private transient String favouriteName;

	/**
	 * Adds the to favourites.
	 * @return the string
	 * @throws Exception the exception
	 */
	public String addToFavourites() throws IOException, RuntimeException {
		final Favourites favourites = this.getFavourites(this.actionId);
		if (favourites == null) {
			final Favourites favourite = new Favourites();
			favourite.setActionId(this.actionId);
			favourite.setUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			favourite.setFavouriteName(this.favouriteName);
			favourite.setContextName(this.baseURL);
			this.favouriteService.create(favourite);
			this.writeToAjaxResponse("SUCCESSFUL");
		} else {
			this.writeToAjaxResponse("ALREADY_EXIST");
		}
		return null;
	}

	/**
	 * Creates the json string.
	 * @param moduleList the module list
	 * @return the string
	 */
	private String createJsonString(final List<Module> moduleList) {
		final StringBuilder jsonString = new StringBuilder();
		if ((moduleList != null) && !moduleList.isEmpty()) {
			final List<Module> favourites = this.moduleDAO.getUserFavourites(Long.valueOf(EGOVThreadLocals.getUserId()));
			jsonString.append("[");
			for (final Module module : moduleList) {
				jsonString.append("{'ModuleName' : '").append(module.getModuleName()).append("',");
				jsonString.append("'ModuleId' : '").append(module.getId()).append("',");
				jsonString.append("'ModuleURL' : '").append(StringUtils.isBlank(module.getContextRoot()) ? "" : "/" + module.getContextRoot()).append(module.getBaseUrl()).append("',");
				jsonString.append("'IsFavourite' : '").append(favourites.contains(module)).append("',");
				jsonString.append("'ModuleChild' : '").append(module.getIsEnabled() ? "" : this.populateAllChildModules(module.getId(), favourites)).append("'},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
		}
		return jsonString.toString();
	}

	/**
	 * Delete from favourites.
	 * @return the string
	 * @throws Exception the exception
	 */
	public String deleteFromFavourites() throws IOException, RuntimeException {
		final Favourites favourites = this.getFavourites(this.actionId);
		if (favourites == null) {
			this.writeToAjaxResponse("DOES_NOT_EXIST");
		} else {
			this.favouriteService.delete(favourites);
			this.writeToAjaxResponse("SUCCESSFUL");
		}
		return null;
	}

	/**
	 * Gets the all modules.
	 * @return the all modules
	 * @throws Exception the exception
	 */
	@Action(value="/common/homepage-submodules")
	public String getAllModules() throws IOException, RuntimeException {
		final List<Module> moduleList = this.moduleDAO.getApplicationModuleByParentId(this.parentId, Long.valueOf(EGOVThreadLocals.getUserId()));
		this.writeToAjaxResponse(this.createJsonString(moduleList));
		return null;
	}

	/**
	 * Gets the favourites.
	 * @param actionId the action id
	 * @return the favourites
	 */
	private Favourites getFavourites(final Integer actionId) {
		return this.favouriteService.find("FROM org.egov.infstr.models.Favourites WHERE  userId = ? and actionId =? ", Integer.valueOf(EGOVThreadLocals.getUserId()), actionId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * Gets the product manifest.
	 * @return the product manifest
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String getProductManifest() throws IOException {
		final StringBuilder productManifest = new StringBuilder();
		try {
			final Manifest manifest = new Manifest(ServletActionContext.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
			final Attributes attributes = manifest.getAttributes("Main");
			productManifest.append(" version ").append(attributes.getValue("Version") == null ? "" : attributes.getValue("Version"));
			productManifest.append(" - build ").append(attributes.getValue("Build-Number") == null ? "" : attributes.getValue("Build-Number"));
		} catch (final Exception e) {
			LOG.warn("Can not read value from MANIFEST file to generate product specification");
		}
		return productManifest.toString();
	}

	/**
	 * Populate all child modules.
	 * @param parentId the parent id
	 * @return the string
	 */
	private String populateAllChildModules(final Integer parentId, final List<Module> favourites) {
		final List<Module> moduleList = this.moduleDAO.getApplicationModuleByParentId(parentId, Long.valueOf(EGOVThreadLocals.getUserId()));
		final StringBuilder childModules = new StringBuilder();
		String url = ServletActionContext.getRequest().getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("common"));
		for (final Module module : moduleList) {
			childModules.append(this.startDynamicNodeCreation(module, favourites, url));
		}
		return childModules.toString();
	}

	/**
	 * Redirect homepage.
	 * @return the string
	 */
	@Override
	public String execute() {
		String status = ERROR;
		try {
			final HttpServletRequest req = ServletActionContext.getRequest();
			final HttpSession session = req.getSession();
			final Boundary boundary = this.boundaryService.getBoundaryById(Long.valueOf((String) session.getAttribute("org.egov.topBndryID")));
			if (boundary == null) {
				throw new EGOVRuntimeException("City does not found");
			}

			final User user = this.userService.getUserById(Long.valueOf(EGOVThreadLocals.getUserId()));
			if (user == null) {
				throw new EGOVRuntimeException("User does not found");
			}
			final String userName = user.getName() == null ? "Anonymous" : user.getName();

			final List<Module> moduleBeanList = this.moduleDAO.getModuleInfoForRoleIds(user.getRoles());
			final List<Module> selfServiceList = this.getEmployeeSelfService(moduleBeanList);
			final List<Module> favouriteList = this.moduleDAO.getUserFavourites(user.getId());
			final Map<String, Object> homePageInfoMap = new HashMap<String, Object>();
			homePageInfoMap.put("moduleBeanList", moduleBeanList);
			homePageInfoMap.put("selfServiceList", selfServiceList);
			homePageInfoMap.put("favouriteList", favouriteList);
			homePageInfoMap.put("imgLeftLogo", req.getContextPath() + "/images/" + session.getAttribute("citylogo"));
			homePageInfoMap.put("imgTiltle", req.getContextPath() + "/images/" + session.getAttribute("citylogo") + "-headerTitle.gif");
			homePageInfoMap.put("cityNameHeading", session.getAttribute("cityname"));
			homePageInfoMap.put("userName", userName);
			homePageInfoMap.put("toplevelbndryName", boundary.getName());
			homePageInfoMap.put("productManifest", this.getProductManifest());
			req.setAttribute("homePageInfoMap", homePageInfoMap);
			status = SUCCESS;
		} catch (final EGOVRuntimeException e) {
			LOG.error("Error occurred while preparing Portal Home Page, Cause : " + e.getMessage(), e);
		} catch (final Exception e) {
			LOG.error("Error occurred while preparing Portal Home Page, Cause : " + e.getMessage(), e);
		}
		return status;
	}

	/**
	 * Gets the employee self service.
	 * @param moduleBeanList the module bean list
	 * @return the employee self service
	 */
	private List<Module> getEmployeeSelfService(final List<Module> moduleBeanList) {
		List<Module> selfServiceList = new ArrayList<Module>();
		final Iterator<Module> moduleIterator = moduleBeanList.iterator();
		while (moduleIterator.hasNext()) {
			final Module module = moduleIterator.next();
			if (module.getModuleName().equals("EmployeeSelfService")) {
				moduleIterator.remove();
				ServletActionContext.getRequest().setAttribute("selfServiceHeader", module.getModuleDescription());
				selfServiceList = this.moduleDAO.getApplicationModuleByParentId(module.getId(), Long.valueOf(EGOVThreadLocals.getUserId()));
			}
		}
		return selfServiceList;
	}

	/**
	 * Sets the action id.
	 * @param actionId the new action id
	 */
	public void setActionId(final Integer actionId) {
		this.actionId = actionId;
	}

	/**
	 * Sets the base url.
	 * @param baseURL the new base url
	 */
	public void setBaseURL(final String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * Sets the boundaryService.
	 * @param boundaryManager the new boundaryService
	 */
	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	/**
	 * Sets the favourite name.
	 * @param favouriteName the new favourite name
	 */
	public void setFavouriteName(final String favouriteName) {
		this.favouriteName = favouriteName;
	}

	/**
	 * Sets the favourite service.
	 * @param favouriteService the favourite service
	 */
	public void setFavouriteService(final PersistenceService<Favourites, Long> favouriteService) {
		this.favouriteService = favouriteService;
	}

	/**
	 * Sets the module dao.
	 * @param moduleDAO the new module dao
	 */
	public void setModuleDAO(final ModuleDao moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	/**
	 * Sets the parent id.
	 * @param parentId the new parent id
	 */
	public void setParentId(final Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * Sets the user manager.
	 * @param userManager the new user manager
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * Start dynamic node creation.
	 * @param module the module
	 * @param url the url
	 * @return the string buffer
	 */
	private StringBuilder startDynamicNodeCreation(final Module module, final List<Module> favourites, final String url) {
		final StringBuilder htmlLayout = new StringBuilder();
		if (module.getIsEnabled()) {
			htmlLayout.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td  valign=\"top\" style=\"background:transparent url(").append(url);
			htmlLayout.append("html/common/image/sm1.gif) repeat-y\"></td></tr>").append("<tr><td valign=\"top\" style=\"background:transparent url(").append(url);
			htmlLayout.append("html/common/image/sm1.gif) repeat-y;padding-right:5px\"><img src=\"../html/common/image/sm2.gif\"  width=\"20\" height=\"17\" /></td><td width=\"94%\">");
			htmlLayout.append((!favourites.contains(module)) ? "<img src=\"../html/common/image/ficon2.gif\" title=\"Add to Favourites\" onclick=\"addToFavourites(event)\" style=\"cursor:pointer\"/>&nbsp;"
					: "<img src=\"../html/common/image/ficon1.gif\" title=\"Remove from Favourites\" onclick=\"removeFromFavourites(event)\" style=\"cursor:pointer\"/>&nbsp;");
			htmlLayout.append("<a name=\"action\" href=\"javascript:void(0);\" class=\"buttonforaccord\" id=\"").append(module.getId()).append("\" ");
			htmlLayout.append("onclick=\"PopupCenter(&quot;").append(StringUtils.isBlank(module.getContextRoot()) ? "" : "/" + module.getContextRoot()).append(module.getBaseUrl()).append("&quot;,&quot;portalApp").append(module.getId()).append("&quot;,");
			htmlLayout.append(850).append(",").append(600).append(")\">").append(module.getModuleName()).append("</a><br/>");
			htmlLayout.append("</td></tr></table>");
		} else {
			htmlLayout.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >");
			htmlLayout.append("<tr><td width=\"1%\" valign=\"top\" style=\"background:transparent url(").append(url);
			htmlLayout.append("html/common/image/sm1.gif) repeat-y;padding-right:5px\"><img src=\"../html/common/image/sm2.gif\"  width=\"20\" height=\"17\" /></td><td>");
			htmlLayout.append("<a href=\"javascript:void(0);\" class=\"normallink buttonforaccord\" id=\"").append(module.getId()).append("\">").append(module.getModuleName()).append("</a>");
			htmlLayout.append(this.populateAllChildModules(module.getId(), favourites));
			htmlLayout.append("</td></tr></table>");
		}
		return htmlLayout;
	}

	/**
	 * Write to ajax response.
	 * @param response the response
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @return
	 */
	private void writeToAjaxResponse(final String response) throws EGOVRuntimeException, IOException {
		final HttpServletResponse httpResponse = ServletActionContext.getResponse();
		httpResponse.getWriter().write(response);
	}
}
