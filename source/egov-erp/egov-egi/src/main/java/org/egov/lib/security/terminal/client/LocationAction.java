/*
 * @(#)LocationAction.java 3.0, 14 Jun, 2013 3:26:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.dao.LocationDAO;
import org.egov.lib.security.terminal.dao.LocationHibernateDAO;
import org.egov.lib.security.terminal.dao.LocationIPMapDAO;
import org.egov.lib.security.terminal.dao.LocationIPMapHibernateDAO;
import org.egov.lib.security.terminal.dao.UserCounterDAO;
import org.egov.lib.security.terminal.dao.UserCounterHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.LocationIPMap;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LocationAction extends DispatchAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(LocationAction.class);

	public ActionForward createLocation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final LocationForm locationform = (LocationForm) form;

			final String name = locationform.getName();
			final String ipaddress[] = locationform.getIpaddress();
			final String loginType = locationform.getLoginType();
			final String counter[] = locationform.getCounter();
			final String ipaddr[] = locationform.getIpaddr();
			final Date createdDate = new java.sql.Date(new java.util.Date().getTime());
			final Timestamp ts = new Timestamp(System.currentTimeMillis());

			if (name == null) {
				throw new EGOVRuntimeException("Location name cannot be null");
			}

			final LocationDAO daoObj = new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession());

			Location obj = new Location();
			obj.setName(name.toUpperCase());
			obj.setDesc(locationform.getDesc());
			obj.setIsActive(Integer.parseInt(locationform.getIsActive()));
			final Set<LocationIPMap> locationIPMapSet = new HashSet<LocationIPMap>();
			if (loginType.equalsIgnoreCase("Location")) {
				obj.setIsLocation(Integer.valueOf(1));
				for (int i = 0; i < ipaddress.length; i++) {
					if (!StringUtils.trimToEmpty(ipaddress[i]).equals("")) {
						/**
						 * checks for unique ipaddress
						 */
						if (daoObj.checkIPAddress(ipaddress[i])) {
							throw new EGOVRuntimeException("IPAddress already exists in the Database");
						}
						final LocationIPMap objIPMap = new LocationIPMap();
						objIPMap.setIpAddress(ipaddress[i]);
						objIPMap.setLocation(obj);
						locationIPMapSet.add(objIPMap);
					}
				}
				if (!locationIPMapSet.isEmpty()) {
					obj.setLocationIPMapSet(locationIPMapSet);
				}

			} else {
				obj.setIsLocation(Integer.valueOf(0));
			}
			obj.setCreatedDate(createdDate);
			obj.setLastModifiedDate(ts);
			obj = (Location) daoObj.create(obj);

			Location counterObj;

			for (int i = 0; i < counter.length; i++) {
				if (!counter[i].trim().equalsIgnoreCase("")) {
					counterObj = new Location();
					/**
					 * checks for unique counter name
					 */
					if (daoObj.checkCounter(counter[i])) {
						throw new EGOVRuntimeException("Counter already exists in the Database");
					}
					counterObj.setName(counter[i].toUpperCase());
					counterObj.setDesc(locationform.getDescription()[i]);
					if (!loginType.equalsIgnoreCase("Location")) {
						if (!StringUtils.trimToEmpty(ipaddr[i]).equals("")) {
							if (daoObj.checkIPAddress(ipaddr[i])) {
								throw new EGOVRuntimeException("IPAddress already exists in the Database");
							}
							final LocationIPMap objIPMap = new LocationIPMap();
							objIPMap.setIpAddress(ipaddr[i]);
							objIPMap.setLocation(counterObj);
							locationIPMapSet.add(objIPMap);
							counterObj.setLocationIPMapSet(locationIPMapSet);
							counterObj.setIsLocation(Integer.valueOf(0));
						}
					} else {
						counterObj.setIsLocation(Integer.valueOf(1));
					}
					counterObj.setIsActive(Integer.parseInt(locationform.getIsActive()));
					counterObj.setLocationId(obj);
					counterObj.setCreatedDate(createdDate);
					counterObj.setLastModifiedDate(ts);
					daoObj.create(counterObj);
				}
			}

			EgovMasterDataCaching.getInstance().removeFromCache("egi-location");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationparent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationIP");
			target = locationform.getForward();

		} catch (final EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred at LocationAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}

	public ActionForward updateLocation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final LocationForm locationform = (LocationForm) form;

			final String id = locationform.getId();
			final String name = locationform.getName();
			final String desc = locationform.getDesc();
			final String isActive = locationform.getIsActive();
			final String ipaddress[] = locationform.getIpaddress();
			final String loginType = locationform.getLoginType();
			final String buttonType = locationform.getForward();
			final String counterId[] = locationform.getCounterId();
			final String counter[] = locationform.getCounter();
			final String ipaddr[] = locationform.getIpaddr();
			final String locIPMapId[] = locationform.getLocIPMapID();
			final String deleteIPSet[] = locationform.getDeleteIPSet();
			final String description[] = locationform.getDescription();
			final Date createdDate = new java.sql.Date(new java.util.Date().getTime());
			final Timestamp ts = new Timestamp(System.currentTimeMillis());

			if (id == null || name == null) {
				throw new EGOVRuntimeException("Location cannot be null");
			}

			final LocationDAO daoObj = new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession());
			Location obj = (Location) daoObj.findById(Integer.parseInt(id), false);
			final LocationIPMapDAO locIPDaoObj = new LocationIPMapHibernateDAO(LocationIPMap.class, HibernateUtil.getCurrentSession());
			if (loginType.equalsIgnoreCase("Location")) {
				for (int i = 0; i < deleteIPSet.length; i++) {
					if (deleteIPSet[i] != null && !deleteIPSet[i].equals("")) {

						final LocationIPMap locIPObj = (LocationIPMap) locIPDaoObj.findById(Integer.parseInt(deleteIPSet[i]), false);
						obj.getLocationIPMapSet().remove(locIPObj);
					}
				}
			}
			obj.setName(name.toUpperCase());
			obj.setDesc(desc);
			obj.setIsActive(Integer.parseInt(isActive));
			Set<LocationIPMap> locIPMapSet = new HashSet<LocationIPMap>();
			final Set<LocationIPMap> locationIPMapSet = new HashSet<LocationIPMap>();

			if (loginType.equalsIgnoreCase("Location")) {
				obj.setIsLocation(Integer.valueOf(1));
				locIPMapSet = obj.getLocationIPMapSet();
				for (final Object element : obj.getLocationIPMapSet()) {
					final LocationIPMap locip = (LocationIPMap) element;
					locationIPMapSet.add(locip);
				}
				for (int j = 0; j < ipaddress.length; j++) {
					boolean insertIP = true;
					for (final LocationIPMap objIPMap : locationIPMapSet) {
						if (locIPMapId[j] != null && !locIPMapId[j].trim().equals("") && Integer.valueOf(locIPMapId[j]).equals(objIPMap.getId())) {
							objIPMap.setIpAddress(ipaddress[j]);
							objIPMap.setLocation(obj);
							locIPMapSet.add(objIPMap);
							insertIP = false;
						}

					}
					if (insertIP == true) {
						final LocationIPMap objIPMapNew = new LocationIPMap();
						objIPMapNew.setIpAddress(ipaddress[j]);
						objIPMapNew.setLocation(obj);
						locIPMapSet.add(objIPMapNew);
					}
				}
				obj.addLocationIPMap(locIPMapSet);

			} else {
				obj.setIsLocation(Integer.valueOf(0));
			}
			obj.setCreatedDate(createdDate);
			obj.setLastModifiedDate(ts);

			obj = (Location) daoObj.update(obj);
			final ArrayList<Location> counterList = (ArrayList<Location>) req.getSession().getAttribute("counterList");
			req.getSession().removeAttribute("counterList");
			for (int i = 0; i < counter.length; i++) {
				boolean insert = true;
				for (final Location loc : counterList) {
					if (counterId[i] != null && !counterId[i].trim().equalsIgnoreCase("") && Integer.parseInt(counterId[i]) == loc.getId()) {
						final Location locobj = (Location) daoObj.findById(Integer.parseInt(counterId[i]), false);
						locobj.setId(Integer.parseInt(counterId[i]));
						locobj.setName(counter[i].toUpperCase());
						locobj.setDesc(description[i]);
						if (!loginType.equalsIgnoreCase("Location")) {
							locIPMapSet = locobj.getLocationIPMapSet();
							for (final Object element : locobj.getLocationIPMapSet()) {
								final LocationIPMap locip = (LocationIPMap) element;
								locationIPMapSet.add(locip);
							}
							locobj.setIsLocation(Integer.valueOf(0));
							for (final LocationIPMap objIPMap : locationIPMapSet) {
								if (locIPMapId[i] != null && !locIPMapId[i].trim().equals("") && Integer.valueOf(locIPMapId[i]).equals(objIPMap.getId())) {
									objIPMap.setLocation(locobj);
									objIPMap.setIpAddress(ipaddr[i]);
									locIPMapSet.add(objIPMap);
								}
							}
							locobj.addLocationIPMap(locIPMapSet);
						} else {
							locobj.setIsLocation(Integer.valueOf(1));
						}
						locobj.setIsActive(Integer.parseInt(isActive));
						locobj.setLocationId(obj);
						locobj.setCreatedDate(createdDate);
						locobj.setLastModifiedDate(ts);
						daoObj.update(locobj);
						insert = false;
					}
				}

				if (insert == true) {
					final Location locobj = new Location();
					locobj.setName(counter[i].toUpperCase());
					locobj.setDesc(description[i]);
					if (!loginType.equalsIgnoreCase("Location")) {
						final LocationIPMap objIPMap = new LocationIPMap();
						objIPMap.setIpAddress(ipaddr[i]);
						objIPMap.setLocation(locobj);
						locIPMapSet.add(objIPMap);
						locobj.addLocationIPMap(locIPMapSet);
						locobj.setIsLocation(Integer.valueOf(0));
					} else {
						locobj.setIsLocation(Integer.valueOf(1));
					}
					locobj.setIsActive(Integer.parseInt(isActive));
					locobj.setLocationId(obj);
					locobj.setCreatedDate(createdDate);
					locobj.setLastModifiedDate(ts);
					daoObj.create(locobj);

				}
			}

				final UserCounterDAO counterObj = new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession());

			for (final Location loc : counterList) {
				boolean present = false;
				final int counterid = loc.getId();

				for (int i = 0; i < counter.length; i++) {
					if (counterId[i] != null && !counterId[i].trim().equalsIgnoreCase("") && Integer.parseInt(counterId[i]) == loc.getId()) {
						present = true;
					}
				}

				if (present == false) {
					if (loginType.equalsIgnoreCase("Terminal")) {
						counterObj.deleteCounters(counterid);
					}
					daoObj.delete(loc);
				}
			}

			EgovMasterDataCaching.getInstance().removeFromCache("egi-location");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationparent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationIP");
			target = buttonType;
		} catch (final EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred at LocationAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}

	public ActionForward deleteLocation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final LocationForm locationform = (LocationForm) form;
			final String id = locationform.getId();
			final String buttonType = locationform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			}

			final LocationDAO daoObj = new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession());

			final ArrayList<Location> list = new ArrayList<Location>(daoObj.findAll());

			for (final Location obj : list) {
				if (obj.getLocationId() != null && obj.getLocationId().getId() == Integer.parseInt(id)) {
					daoObj.delete(obj);
				}
			}

			final Location obj = (Location) daoObj.findById(Integer.parseInt(id), false);
			daoObj.delete(obj);

			EgovMasterDataCaching.getInstance().removeFromCache("egi-location");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationparent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationIP");
			target = buttonType;
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred at LocationAction ", ex);
			throw new EGOVRuntimeException("Exception occurred at LocationAction ", ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward loadModifyData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final LocationForm locationform = (LocationForm) form;
			final String id = locationform.getId();
			final String buttonType = locationform.getForward();

			if (id == null) {
				throw new EGOVRuntimeException("Location cannot be null");
			}

			final LocationDAO daoObj = new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession());
			final Location obj = (Location) daoObj.findById(Integer.parseInt(id), false);

			locationform.setId(Integer.toString(obj.getId()));
			locationform.setName(obj.getName());
			locationform.setDesc(obj.getDesc());
			locationform.setIsActive(obj.getIsActive().toString());
			String loginType = "";
			if (Integer.valueOf(1).equals(obj.getIsLocation())) {
				loginType = "location";

				final Set<LocationIPMap> locationIPMap = obj.getLocationIPMapSet();
				String[] locIPMapArray = null;
				String[] locIPMapIDArray = null;
				if (locationIPMap != null) {
					locIPMapArray = new String[locationIPMap.size()];
					locIPMapIDArray = new String[locationIPMap.size()];
					int j = 0;

					for (final LocationIPMap locIPMap : locationIPMap) {
						locIPMapArray[j] = locIPMap.getIpAddress();
						locIPMapIDArray[j] = locIPMap.getId().toString();
						j++;
					}
				}
				locationform.setIpaddress(locIPMapArray);
				locationform.setLocIPMapID(locIPMapIDArray);
			} else {
				loginType = "terminal";
			}

			final ArrayList<Location> counterList = daoObj.getCountersByLocation(Integer.parseInt(id));

			final String[] counter = new String[counterList.size()];
			final String[] counterId = new String[counterList.size()];
			final String[] ipaddress = new String[counterList.size()];
			final String[] description = new String[counterList.size()];
			final String[] locIPMapIDArray = new String[counterList.size()];

			int i = 0;

			for (final Location loc : counterList) {
				counter[i] = loc.getName();
				counterId[i] = Integer.toString(loc.getId());
				if (loc.getDesc() != null) {
					description[i] = loc.getDesc();
				} else {
					description[i] = "";
				}

				if (loginType.equalsIgnoreCase("terminal")) {
					final Set<LocationIPMap> locationIPMap = loc.getLocationIPMapSet();
					if (locationIPMap != null) {
						for (final LocationIPMap locIPMap : locationIPMap) {
							ipaddress[i] = locIPMap.getIpAddress();
							locIPMapIDArray[i] = locIPMap.getId().toString();
						}
					}
					locationform.setLocIPMapID(locIPMapIDArray);
				}

				i++;
			}

			locationform.setCounterId(counterId);
			locationform.setCounter(counter);
			locationform.setDescription(description);
			locationform.setIpaddr(ipaddress);

			req.getSession().setAttribute("counterList", counterList);
			req.setAttribute("loginType", loginType);
			req.setAttribute("locationform", locationform);
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("locationIPList", EgovMasterDataCaching.getInstance().get("egi-locationIP"));

		} catch (final EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred at LocationAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}

	public ActionForward loadCreateData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			final LocationForm locationform = (LocationForm) form;
			final String buttonType = locationform.getForward();
			req.setAttribute("buttonType", buttonType);
			target = buttonType;
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("locationIPList", EgovMasterDataCaching.getInstance().get("egi-locationIP"));
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred at LocationAction ", ex);
			throw new EGOVRuntimeException("Exception occurred at LocationAction ", ex);
		}
		return mapping.findForward(target);
	}

}
