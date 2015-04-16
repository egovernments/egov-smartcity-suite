/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lib.security.terminal.client;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.security.terminal.dao.LocationHibernateDAO;
import org.egov.lib.security.terminal.dao.UserCounterHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCounterMapAction extends DispatchAction {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(UserCounterMapAction.class);

	private UserCounterHibernateDAO userCounterHibernateDAO;
	private LocationHibernateDAO locationHibernateDAO;
	private UserService userService;

	public UserCounterMapAction(UserCounterHibernateDAO userCounterHibernateDAO, LocationHibernateDAO locationHibernateDAO, UserRepository userRepository) {
		this.userCounterHibernateDAO = userCounterHibernateDAO;
		this.locationHibernateDAO = locationHibernateDAO;
		this.userService = userService;
	}

	public ActionForward createUserControlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			
			String buttonType = usercountermapform.getForward();
			String locationId = usercountermapform.getLocationId();
			String counter[] = usercountermapform.getCounter();
			String loginType = usercountermapform.getLoginType();
			Integer modifiedBy = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			
			if (loginType.equalsIgnoreCase("Location")) {
				if (locationId == null)
					throw new EGOVRuntimeException("ActionForm value cannot be null");
				
				String userId[] = usercountermapform.getUserId();
				if (userId != null) {
					for (int i = 0; i < userId.length; i++) {
						if (userId[i] != null && !userId[i].trim().equals("")) {
							Location locationobj = (Location) locationHibernateDAO.findById(Integer.parseInt(locationId), false);
							User userobj = (User) userService.getUserById(Long.valueOf(userId[i]));
							if (usercountermapform.getSelCheck()[i].equals("yes") && usercountermapform.getUserCounterId()[i] != null && !usercountermapform.getUserCounterId()[i].equals("")) {
								UserCounterMap obj = (UserCounterMap) userCounterHibernateDAO.findById(Integer.parseInt(usercountermapform.getUserCounterId()[i]), false);
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								userCounterHibernateDAO.update(obj);
							} else if (usercountermapform.getSelCheck()[i].equals("no") && (usercountermapform.getUserCounterId()[i] == null || usercountermapform.getUserCounterId()[i].equals(""))) {
								UserCounterMap obj = new UserCounterMap();
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								userCounterHibernateDAO.create(obj);
							}
							
						}
					}
				}
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				String userId[] = usercountermapform.getUserId();
				if (userId != null && counter != null) {
					for (int i = 0; i < counter.length; i++) {
						if (userId[i] != null && counter[i] != null && !userId[i].trim().equals("") && !counter[i].trim().equals("")) {
							
							Location locationobj = (Location) locationHibernateDAO.findById(Integer.parseInt(counter[i]), false);
							User userobj = (User) userService.getUserById(Long.valueOf(userId[i]));
							if (usercountermapform.getSelCheck()[i].equals("yes") && usercountermapform.getUserCounterId()[i] != null && !usercountermapform.getUserCounterId()[i].equals("")) {
								
								UserCounterMap obj = (UserCounterMap) userCounterHibernateDAO.findById(Integer.parseInt(usercountermapform.getUserCounterId()[i]), false);
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								userCounterHibernateDAO.update(obj);
							} else if (usercountermapform.getSelCheck()[i].equals("no") && (usercountermapform.getUserCounterId()[i] == null || usercountermapform.getUserCounterId()[i].equals(""))) {
								
								UserCounterMap obj = new UserCounterMap();
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								userCounterHibernateDAO.create(obj);
							}
							
						}
					}
				}
			}
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-location");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationparent");
			target = buttonType;
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
	
	public ActionForward getUserCounterForCurrentDate(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			String loginType = usercountermapform.getLoginType();
			Location locationobj = (Location) locationHibernateDAO.findById(Integer.valueOf(usercountermapform.getLocationId()), false);
			if (loginType.equalsIgnoreCase("Location")) {
				List userCounterMapList = userCounterHibernateDAO.getLocationBasedUserCounterMapForCurrentDate(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				List userCounterMapList = userCounterHibernateDAO.getTerminalBasedUserCounterMapForCurrentDate(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			}
			List userList = Collections.emptyList();//userService.getAllUserForRoles(EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN"), new java.util.Date());
			
			req.setAttribute("userList", userList);
			req.setAttribute("locationParentList", EgovMasterDataCaching.getInstance().get("egi-locationparent"));
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("locationobj", locationobj);
			req.setAttribute("loginType", loginType);
			target = "currentDate";
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
	
	public ActionForward getAllUserCounters(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			String loginType = usercountermapform.getLoginType();
			if (loginType.equalsIgnoreCase("Location")) {
				List userCounterMapList = userCounterHibernateDAO.getUserCounterMapForLocationId(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				List userCounterMapList = userCounterHibernateDAO.getUserCounterMapForTerminalId(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			}
			String buttonType = "showAll";
			Location locationobj = (Location) locationHibernateDAO.findById(Integer.valueOf(usercountermapform.getLocationId()), false);
			List userList = Collections.emptyList();// userService.getAllUserForRoles(EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN"), new java.util.Date());
			
			req.setAttribute("userList", userList);
			req.setAttribute("locationParentList", EgovMasterDataCaching.getInstance().get("egi-locationparent"));
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("buttonType", buttonType);
			req.setAttribute("locationobj", locationobj);
			req.setAttribute("loginType", loginType);
			target = buttonType;
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
}
