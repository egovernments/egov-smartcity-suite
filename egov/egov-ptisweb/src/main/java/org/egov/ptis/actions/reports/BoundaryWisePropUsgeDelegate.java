/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.utils.MoneyUtils.roundOff;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;

@Transactional(readOnly = true)
public class BoundaryWisePropUsgeDelegate {
	private static final Logger LOGGER = Logger.getLogger(BoundaryWisePropUsgeDelegate.class);
	@Autowired
	PropertyUsageDAO propertyUsageDAO;
	@Autowired
	PropertyTypeMasterDAO propertyTypeMasterDAO;
	@Autowired
	PropertyDAO propertyDAO;

	@Autowired
	private BoundaryService boundaryService;

	/**
	 * To get the Map of Zone wise property usage and Demand in the current
	 * financial year. i.e(Map(zone,Map(propertyUsage,Bean)))
	 * 
	 * @param java
	 *            .util.List boundaryList
	 * @param Integer
	 *            bndryNo
	 * 
	 * @return Map.
	 * 
	 */
	public Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> getBoundaryWiseList(
			List boundaryList, Integer bndryNo, List<PropertyTypeMaster> propMstrList) {
		LOGGER.debug("Entered into getBoundaryWiseList method");
		LOGGER.debug("Boundary number : " + bndryNo + ", " + "Boundary List : "
				+ (boundaryList != null ? boundaryList : ZERO));
		TreeMap<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> boundaryMap = null;
		TreeMap<Integer, BoundryWisePropUsgeBean> propUsageIDMap = null;
		TreeMap<Integer, BoundryWisePropUsgeBean> propUsag = null;
		BigDecimal aggArrearDmdTot = ZERO;
		BigDecimal aggCurrentDmdTot = ZERO;
		Integer aggTotalProps = 0;
		BoundryWisePropUsgeBean bndryBean, indTotBean = null;
		Object[] outBndryObj;
		Object[] innrBndryObj;
		// this propAggTot is used for sorting purpose in TreeMap.This is used
		// as propertyUsage ,and assigned to Aggregate total i.e row wise
		// Aggregate Total
		// this propIndTotNum is used for sorting purpose in TreeMap.This is
		// used for Zone,and assigned to column wise aggregate total
		// this propNullId is used for sorting purpose in TreeMap.This is used
		// for N/A Usage
		int propAggTot = 1900;
		int propIndTotNum = 5000;
		int propNullId = 900;
		if (boundaryList != null && !boundaryList.isEmpty()) {
			boundaryMap = new TreeMap<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>>();
			for (int i = 0; i < boundaryList.size();) {
				propNullId = propNullId + 1;
				outBndryObj = (Object[]) boundaryList.get(i);
				propUsageIDMap = new TreeMap<Integer, BoundryWisePropUsgeBean>();
				// Initialising Property Usage map with Dummy values
				prpUsageMapWithDmyVals(propUsageIDMap, null, propMstrList);
				// propNAUsageMapWithDmyVals(propUsageIDMap, propNullId, null);
				aggArrearDmdTot = ZERO;
				aggCurrentDmdTot = ZERO;
				BigDecimal innerBndryObj = ZERO;
				innerBndryObj.setScale(2);
				aggTotalProps = 0;

				for (int j = i; j < boundaryList.size(); j++, i++) {
					innrBndryObj = (Object[]) boundaryList.get(j);
					if (innrBndryObj[0].equals(outBndryObj[0])) {
						Integer propTypeId = Integer.valueOf(((Long) innrBndryObj[1]).intValue());
						if (propTypeId == null || propTypeId.equals("")) {
							bndryBean = new BoundryWisePropUsgeBean();
							prpUsageMap(bndryBean, innrBndryObj, propUsageIDMap, null, propNullId);
							aggTotalProps = aggTotalProps + (Integer) innrBndryObj[4];
							aggArrearDmdTot = aggArrearDmdTot.add((BigDecimal) innrBndryObj[2]);
							aggCurrentDmdTot = aggCurrentDmdTot.add((BigDecimal) innrBndryObj[3]);

						} else {
							if (propUsageIDMap.containsKey(propTypeId)) {
								bndryBean = propUsageIDMap.get(propTypeId);
								prpUsageMap(bndryBean, innrBndryObj, propUsageIDMap, "withUsage",
										null);
								aggTotalProps = aggTotalProps + (Integer) innrBndryObj[4];
								aggArrearDmdTot = aggArrearDmdTot.add((BigDecimal) innrBndryObj[2]);
								aggCurrentDmdTot = aggCurrentDmdTot
										.add((BigDecimal) innrBndryObj[3]);
							}

						}
						LOGGER.debug("Aggregate Total Properties : " + aggTotalProps + ", "
								+ "Aggregate Total Arrears Demand : " + aggArrearDmdTot + ", "
								+ "Aggregate Total Current Demand : " + aggCurrentDmdTot);
					} else {
						break;
					}
				}
				bndryBean = new BoundryWisePropUsgeBean();
				bndryBean.setArrDmd(roundOff(aggArrearDmdTot));
				bndryBean.setCurrDmd(roundOff(aggCurrentDmdTot));
				bndryBean.setPropCount(aggTotalProps);
				bndryBean.setTotalDemand(roundOff(aggArrearDmdTot.add(aggCurrentDmdTot)));
				LOGGER.debug("Total Properties : " + bndryBean.getPropCount() + ", " + 
							"Arrears Demand : " + bndryBean.getArrDmd() + ", " + 
							"Current Demand : " + bndryBean.getArrDmd() + ", " + 
							"Total Demand : " + bndryBean.getTotalDemand());
				propUsageIDMap.put(propAggTot + 1, bndryBean);
				boundaryMap.put((Integer) outBndryObj[0], propUsageIDMap);

			}
			List propIndTotList = getTotPropUsage(bndryNo);
			LOGGER.debug("propIndTotList size : "
					+ (propIndTotList != null ? propIndTotList.size() : ZERO));
			if (propIndTotList != null && !propIndTotList.isEmpty()) {
				int propUsageIndTot = 2500;
				propUsag = new TreeMap<Integer, BoundryWisePropUsgeBean>();
				prpUsageMapWithDmyVals(null, propUsag, propMstrList);
				propNAUsageMapWithDmyVals(null, propNullId, propUsag, propMstrList);
				BoundryWisePropUsgeBean indAggTotBean = null;
				BigDecimal indAggDemand = ZERO;
				BigDecimal indCurrDemand = ZERO;
				BigDecimal indTotalDemand = ZERO;
				Integer indaggPropCnt = 0;
				for (Object propObject : propIndTotList) {
					Object totList[] = (Object[]) propObject;
					BigDecimal totalDemand = ZERO;
					Integer indPropCount = 0;
					if (totList[0] == null || totList[0].equals("")) {
						indPropCount = propNullId;
						indAggDemand = indAggDemand.add((BigDecimal) totList[2]);
						indCurrDemand = indCurrDemand.add((BigDecimal) totList[3]);
						indTotalDemand = indTotalDemand.add((BigDecimal) totList[2]).add(
								(BigDecimal) totList[3]);
						indaggPropCnt = indaggPropCnt + (Integer) totList[1];

					} else {
						indPropCount = Integer.valueOf(((Long) totList[0]).intValue());
						indAggDemand = indAggDemand.add((BigDecimal) totList[2]);
						indCurrDemand = indCurrDemand.add((BigDecimal) totList[3]);
						indTotalDemand = indTotalDemand.add((BigDecimal) totList[2]).add(
								(BigDecimal) totList[3]);
						indaggPropCnt = indaggPropCnt + (Integer) totList[1];

					}
					indTotBean = new BoundryWisePropUsgeBean();
					indTotBean.setPropCount((Integer) totList[1]);
					indTotBean.setArrDmd(roundOff((BigDecimal) totList[2]));
					indTotBean.setCurrDmd(roundOff((BigDecimal) totList[3]));
					totalDemand = totalDemand.add((BigDecimal) totList[2]).add((BigDecimal) totList[3]);
					indTotBean.setTotalDemand(roundOff(totalDemand));
					propUsag.put(indPropCount, indTotBean);
					LOGGER.debug("Individual Aggregate Property count : " + indaggPropCnt + ", "
							+ "Individual Current Demand : " + indCurrDemand + ", "
							+ "Individual Arrears Demand : " + indAggDemand + ", "
							+ "Individual Total Demand : " + indTotalDemand);
				}
				indAggTotBean = new BoundryWisePropUsgeBean();
				indAggTotBean.setArrDmd(roundOff(indAggDemand));
				indAggTotBean.setCurrDmd(roundOff(indCurrDemand));
				indAggTotBean.setTotalDemand(roundOff(indTotalDemand));
				indAggTotBean.setPropCount(indaggPropCnt);
				LOGGER.debug("Total Properties : " + indAggTotBean.getPropCount() + ", "
						+ "Arrears Demand : " + indAggTotBean.getArrDmd() + ", "
						+ "Current Demand : " + indAggTotBean.getCurrDmd() + ", "
						+ "Total Demand : " + indAggTotBean.getTotalDemand());
				propUsag.put(propUsageIndTot, indAggTotBean);
				boundaryMap.put(propIndTotNum, propUsag);
			}
		}
		LOGGER.debug("Boundary Map : " + (boundaryMap != null ? boundaryMap : ZERO));
		LOGGER.debug("Exit from getBoundaryWiseList method");
		return boundaryMap;
	}

	/**
	 * To set the values to the BoundryWisePropUsgeBean i.e(arrear
	 * demand,current demand,total demand,av amount etc) . This method is called
	 * internally from the method getBoundaryWiseList(List boundaryList,Integer
	 * bndryNo)
	 * 
	 * @param org
	 *            .egov.ptis.property.client.struts2.reports.coc.
	 *            BoundryWisePropUsgeBean bndryBean
	 * @param java
	 *            .lang.Object[] bndryObj
	 * @param java
	 *            .util.TreeMap<Integer, BoundryWisePropUsgeBean> propUsageIDMap
	 * @param java
	 *            .lang.String usage
	 * @param java
	 *            .lang.Integer propNullId
	 * 
	 */
	public void prpUsageMap(BoundryWisePropUsgeBean bndryBean, Object[] bndryObj,
			Map<Integer, BoundryWisePropUsgeBean> propUsageIDMap, String usgage, Integer propNullId) {
		LOGGER.debug("Entered into prpUsageMap method");
		BigDecimal totalDemand = ZERO;
		BigDecimal arrDmd = ZERO;
		BigDecimal currDmd = ZERO;
		Integer propCount = 0;
		Integer propId = 0;
		arrDmd = arrDmd.add((BigDecimal) bndryObj[2]);
		currDmd = currDmd.add((BigDecimal) bndryObj[3]);
		BigDecimal bndryObjAvAmt = ZERO;
		totalDemand = totalDemand.add(arrDmd).add(currDmd);
		propCount = (Integer) bndryObj[4];
		if (usgage == null) {
			propId = propNullId;
		} else if (usgage != null && usgage.equals("withUsage")) {
			propId = Integer.valueOf(((Long) bndryObj[1]).intValue());
		}
		bndryBean.setArrDmd(roundOff(arrDmd));
		bndryBean.setCurrDmd(roundOff(currDmd));
		bndryBean.setPropCount(propCount);
		bndryBean.setTotalDemand(roundOff(totalDemand));
		propUsageIDMap.put(propId, bndryBean);
		LOGGER.debug("Property Id : " + propId + ", " + "Total Properties : "
				+ bndryBean.getPropCount() + ", " + "Arrears Demand : " + bndryBean.getArrDmd()
				+ ", " + "Current Demand : " + bndryBean.getCurrDmd() + ", " + "Total Demand : "
				+ bndryBean.getTotalDemand());
		LOGGER.debug("Exit from prpUsageMap method");
	}

	/**
	 * To set the zero values to the BoundryWisePropUsgeBean i.e(arrear
	 * demand,current demand,total demand,av amount etc) . This method is called
	 * internally from the method getBoundaryWiseList(List boundaryList,Integer
	 * bndryNo) Bean values are initialised with zero values because if for a
	 * zone if there is no data for any usage then in result screen Zero needs
	 * to be shown.
	 * 
	 * @param org
	 *            .egov.ptis.property.client.struts2.reports.coc.
	 *            BoundryWisePropUsgeBean bndryBean
	 * @param java
	 *            .lang.Object[] bndryObj
	 * @param java
	 *            .util.TreeMap<Integer, BoundryWisePropUsgeBean> propUsageIDMap
	 * @param java
	 *            .lang.String usage
	 * @param java
	 *            .lang.Integer propNullId
	 * 
	 */

	public void prpUsageMapWithDmyVals(Map<Integer, BoundryWisePropUsgeBean> propUsageIDMap,
			Map<Integer, BoundryWisePropUsgeBean> propUsag, List<PropertyTypeMaster> propMstrList) {
		LOGGER.debug("Entered into prpUsageMapWithDmyVals method");
		LOGGER.debug("PropertyUsageId Map size : "
				+ (propUsageIDMap != null ? propUsageIDMap.size() : ZERO));
		LOGGER.debug("PropertyUsage Map size : " + (propUsag != null ? propUsag.size() : ZERO));
		// List allPropUsgList = propUsageDao.getPropUsageAscOrder();
		LOGGER.debug("All Property Usage List : " + (propMstrList != null ? propMstrList : ZERO));
		for (int propUsge = 0; propUsge < propMstrList.size(); propUsge++) {
			BoundryWisePropUsgeBean beanWithZeroVal = new BoundryWisePropUsgeBean();
			beanWithZeroVal.setArrDmd(ZERO);
			beanWithZeroVal.setCurrDmd(ZERO);
			beanWithZeroVal.setPropCount(0);
			beanWithZeroVal.setTotalDemand(ZERO);
			LOGGER.debug("Total Properties : " + beanWithZeroVal.getPropCount() + ", "
					+ "Arrears Demand : " + beanWithZeroVal.getArrDmd() + ", "
					+ "Current Demand : " + beanWithZeroVal.getCurrDmd() + ", " + "Total Demand : "
					+ beanWithZeroVal.getTotalDemand());
			if (propUsageIDMap == null) {
				propUsag.put(propMstrList.get(propUsge).getId().intValue(),
						beanWithZeroVal);
			} else {
				propUsageIDMap.put(propMstrList.get(propUsge).getId()
						.intValue(), beanWithZeroVal);
			}
		}
		LOGGER.debug("Exit from prpUsageMapWithDmyVals method");
	}

	public void propNAUsageMapWithDmyVals(Map<Integer, BoundryWisePropUsgeBean> propUsageIDMap,
			Integer propNAUsgeId, Map<Integer, BoundryWisePropUsgeBean> propUsag,
			List<PropertyTypeMaster> propMstrList) {
		LOGGER.debug("Entered into propNAUsageMapWithDmyVals method");
		LOGGER.debug("PropertyUsageId Map size : "
				+ (propUsageIDMap != null ? propUsageIDMap.size() : ZERO));
		LOGGER.debug("PropetyNAUsageId : " + propNAUsgeId);
		LOGGER.debug("PropertyUsage Map size : " + (propUsag != null ? propUsag.size() : ZERO));
		// List<PropertyUsage> allPropUsgList =
		// propUsageDao.getPropUsageAscOrder();
		for (int propUsge = 0; propUsge < propMstrList.size(); propUsge++) {
			BoundryWisePropUsgeBean beanWithZeroVal = new BoundryWisePropUsgeBean();
			beanWithZeroVal.setArrDmd(ZERO);
			beanWithZeroVal.setCurrDmd(ZERO);
			beanWithZeroVal.setPropCount(0);
			beanWithZeroVal.setTotalDemand(ZERO);
			LOGGER.debug("Total Properties : " + beanWithZeroVal.getPropCount() + ", "
					+ "Arrears Demand : " + beanWithZeroVal.getArrDmd() + ", "
					+ "Current Demand : " + beanWithZeroVal.getCurrDmd() + ", " + "Total Demand : "
					+ beanWithZeroVal.getTotalDemand());
			if (propUsageIDMap == null) {
				propUsag.put(propNAUsgeId, beanWithZeroVal);
			} else {
				propUsageIDMap.put(propNAUsgeId, beanWithZeroVal);
			}
		}
		LOGGER.debug("Exit from propNAUsageMapWithDmyVals method");
	}

	public List getZoneList() {
		LOGGER.debug("Entered into getZoneList method");
		List zoneList = null;
		Criterion criterion = null;
		Projection projection = Projections.projectionList().add(Projections.property("zone.id"))
				.add(Projections.property("propTypeMstrID.id")).add(Projections.sum("aggrArrDmd"))
				.add(Projections.sum("aggrCurrDmd")).add(Projections.count("basicPropertyID"))
				.add(Projections.groupProperty("zone.id"))
				.add(Projections.groupProperty("propTypeMstrID"));
		Order order = Order.asc("zone.id");

		/*
		 * Integer vacTypeId = getPropertyIdbyCode("OPEN_PLOT"); criterion =
		 * Restrictions.ne("propTypeMstrID", vacTypeId);
		 */
		zoneList = propertyDAO.getPropMaterlizeViewList(projection, criterion, order);
		LOGGER.debug("Zone list : " + (zoneList != null ? zoneList : ZERO));
		LOGGER.debug("Exit from getZoneList method");
		return zoneList;
	}

	public List getTotPropUsage(Integer bndryNo) {
		LOGGER.debug("Entered into getTotPropUsage method");
		LOGGER.debug("Boundary Number : " + bndryNo);
		List wardList = null;
		Criterion criterion = null;
		Criterion vacantCrit = null;
		Conjunction conjun = Restrictions.conjunction();
		Projection projection = Projections.projectionList()
				.add(Projections.property("propTypeMstrID.id"))
				.add(Projections.count("basicPropertyID")).add(Projections.sum("aggrArrDmd"))
				.add(Projections.sum("aggrCurrDmd"))
				.add(Projections.groupProperty("propTypeMstrID"));
		if (bndryNo != null) {
			criterion = Restrictions.like("zone.id", bndryNo);
			conjun.add(criterion);
		}
		/*
		 * Integer vacTypeId = getPropertyIdbyCode("OPEN_PLOT"); vacantCrit =
		 * Restrictions.ne("propTypeMstrID", vacTypeId); conjun.add(vacantCrit);
		 */
		wardList = propertyDAO.getPropMaterlizeViewList(projection, conjun, null);
		LOGGER.debug("Ward list : " + (wardList != null ? wardList : ZERO));
		LOGGER.debug("Exit from getTotPropUsage method");
		return wardList;
	}

	public List getWardList(Integer zoneNo) {
		LOGGER.debug("Entered into getWardList method");
		LOGGER.debug("Zone Number : " + zoneNo);
		List wardList = null;
		Conjunction conjun = Restrictions.conjunction();
		if (zoneNo > 0) {
			Criterion criterion = Restrictions.like("zone.id", zoneNo);
			conjun.add(criterion);
			Integer vacTypeId = getPropertyIdbyCode("OPEN_PLOT");
			// Criterion anothercriterion = Restrictions.ne("propTypeMstrID",
			// vacTypeId);
			// conjun.add(anothercriterion);

			Projection projection = Projections.projectionList()
					.add(Projections.property("ward.id"))
					.add(Projections.property("propTypeMstrID.id"))
					.add(Projections.sum("aggrArrDmd")).add(Projections.sum("aggrCurrDmd"))
					.add(Projections.count("basicPropertyID"))
					.add(Projections.groupProperty("ward.id"))
					.add(Projections.groupProperty("propTypeMstrID"));
			Order order = Order.asc("ward.id");
			wardList = propertyDAO.getPropMaterlizeViewList(projection, conjun, order);
		}
		LOGGER.debug("Ward list : " + (wardList != null ? wardList : ZERO));
		LOGGER.debug("Exit from getWardList method");
		return wardList;
	}

	public String getBndryNameById(long bndryID) {
		LOGGER.debug("Entered into getBndryNameById method");
		LOGGER.debug("Boundary Id : " + bndryID);
		String bndName = null;
		if (bndryID > 0) {
			Boundary bndryObj = boundaryService.getBoundaryById(bndryID);
			if (bndryObj != null) {
				bndName = bndryObj.getName();
			}
		}
		LOGGER.debug("Boundary Name : " + bndName);
		LOGGER.debug("Exit from getBndryNameById method");
		return bndName;
	}

	private Integer getPropertyIdbyCode(String code) {
		LOGGER.debug("Entered into getPropertyIdbyCode method");
		PropertyTypeMaster vacMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode(code);
		LOGGER.debug("Exit from getPropertyIdbyCode method");
		return vacMaster.getId().intValue();

	}

	/**
	 * Called to get zonewise, property type wise sum of demands
	 * 
	 * @param zonePropTypeMap
	 *            zone and usage wise demand list
	 * @return zone and property type wise demand lsit
	 */

	public Map<Integer, Map<String, BoundryWisePropUsgeBean>> getZoneAndPropertyTypeWiseList(
			Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> zonePropTypeMap) {
		LOGGER.debug("Entered into getZoneAndPropertyTypeWiseList method");
		LOGGER.debug("ZonePropTypeMap : " + (zonePropTypeMap != null ? zonePropTypeMap : ZERO));

		Map<Integer, Map<String, BoundryWisePropUsgeBean>> zonePropertyTypeMap = null;
		Map<String, BoundryWisePropUsgeBean> propTypeMap = null;
		zonePropertyTypeMap = new TreeMap<Integer, Map<String, BoundryWisePropUsgeBean>>();

		for (Integer zoneId : zonePropTypeMap.keySet()) {
			LOGGER.debug("Zone Id : " + zoneId);
			TreeMap<Integer, BoundryWisePropUsgeBean> propTypesMap = zonePropTypeMap.get(zoneId);
			LOGGER.debug("propTypesMap: " + (propTypesMap != null ? propTypesMap : ZERO));
			propTypeMap = new TreeMap<String, BoundryWisePropUsgeBean>();
			propTypeMap = initPropTypeMap(propTypeMap);
			for (Integer propTypeId : propTypesMap.keySet()) {
				LOGGER.debug("propTypeId : " + propTypeId);
				if (propTypeId > 1900) {
					propTypeMap.put("TotalAgg", propTypesMap.get(propTypeId));
				} else if (propTypeId < 1900 && propTypeId > 900) {
					// propTypeMap.put("NoUsage", propTypesMap.get(propTypeId));
					continue;
				} else {
					/*
					 * PropertyTypeMaster propTypeMstr = (PropertyTypeMaster)
					 * propTypeMstrDao.findById(propTypeId.longValue(), false);
					 * String propTypeName = propTypeMstr.getType();
					 * LOGGER.debug("PropertyType Name : " + propTypeName); if
					 * (USAGES_FOR_RESD.contains(propTypeName) ||
					 * USAGES_FOR_NON_RESD.contains(propTypeName) ||
					 * USAGES_FOR_OPENPLOT.contains(propTypeName)) {
					 * sumUsagesByCategory(getPropertyType(propTypeName),
					 * propTypeMap, usagesMap.get(usageId)); }
					 */
					propTypeMap.put(propTypeId.toString(), propTypesMap.get(propTypeId));
				}

			}
			LOGGER.debug("PropertyType map : " + (propTypeMap != null ? propTypeMap : ZERO));
			zonePropertyTypeMap.put(zoneId, propTypeMap);
		}
		LOGGER.debug("ZonePropertyType map : "
				+ (zonePropertyTypeMap != null ? zonePropertyTypeMap : ZERO));
		LOGGER.debug("Exit from getZoneAndPropertyTypeWiseList method");
		return zonePropertyTypeMap;
	}

	/**
	 * Called locally to sum up existing properties with property of same
	 * property type
	 * 
	 * @param propType
	 * @param propTypeMap
	 * @param bean
	 */
	private void sumUsagesByCategory(String propType,
			Map<String, BoundryWisePropUsgeBean> propTypeMap, BoundryWisePropUsgeBean bean) {
		LOGGER.debug("Entered into sumUsagesByCategory method");
		LOGGER.debug("Property Type : " + propType);
		LOGGER.debug("PropTypeMap : " + (propTypeMap != null ? propTypeMap : ZERO));

		BoundryWisePropUsgeBean newBean = new BoundryWisePropUsgeBean();
		BoundryWisePropUsgeBean existingBean = propTypeMap.get(propType);
		LOGGER.debug("Existing Bean Details : ");
		LOGGER.debug("Total Properties : " + existingBean.getPropCount() + ", "
				+ "Arrears Demand : " + existingBean.getArrDmd() + ", " + "Current Demand : "
				+ existingBean.getCurrDmd() + ", " + "Total Demand : "
				+ existingBean.getTotalDemand());
		newBean.setArrDmd(existingBean.getArrDmd().add(bean.getArrDmd()));
		newBean.setCurrDmd(existingBean.getCurrDmd().add(bean.getCurrDmd()));

		newBean.setTotalDemand(existingBean.getTotalDemand().add(bean.getTotalDemand()));
		newBean.setPropCount(existingBean.getPropCount() + bean.getPropCount());

		propTypeMap.remove(propType);
		propTypeMap.put(propType, newBean);
		LOGGER.debug("New Bean Details : ");
		LOGGER.debug("Total Properties : " + newBean.getPropCount() + ", " + "Arrears Demand : "
				+ newBean.getArrDmd() + ", " + "Current Demand : " + newBean.getCurrDmd() + ", "
				+ "Total Demand : " + newBean.getTotalDemand());
		LOGGER.debug("Exit from sumUsagesByCategory method");
	}

	/**
	 * Called locally to get type of property on usage name
	 * 
	 * @param usageId
	 * @return String propertyType
	 */
	@SuppressWarnings("unchecked")
	private String getPropertyType(String usageName) {

		LOGGER.debug("Entered into getPropertyType method");
		LOGGER.debug("Usage Name : " + usageName);
		String propertyType = null;

		if (usageName != null) {

			if (USAGES_FOR_RESD.contains(usageName)) {
				propertyType = USAGES_FOR_RESD;
			} else if (USAGES_FOR_NON_RESD.contains(usageName)) {
				propertyType = USAGES_FOR_NON_RESD;
			} else if (USAGES_FOR_OPENPLOT.contains(usageName)) {
				propertyType = USAGES_FOR_OPENPLOT;
			}

		}
		LOGGER.debug("Property Type : " + propertyType);
		LOGGER.debug("Exit from getPropertyType method");
		return propertyType;
	}

	/**
	 * Called locally to initialize the propTypeMap properties(arrDmd, currDmd,
	 * avAmt, totalDemand and propCount) to 0
	 * 
	 * @param propTypeMap
	 * @return propTypeMap
	 */

	private Map<String, BoundryWisePropUsgeBean> initPropTypeMap(
			Map<String, BoundryWisePropUsgeBean> propTypeMap) {

		LOGGER.debug("Entered into initPropTypeMap method");
		LOGGER.debug("PropTypeMap : " + (propTypeMap != null ? propTypeMap : ZERO));
		BoundryWisePropUsgeBean bean = new BoundryWisePropUsgeBean();

		bean.setArrDmd(ZERO);
		bean.setCurrDmd(ZERO);
		bean.setTotalDemand(ZERO);
		bean.setPropCount(0);
		List<PropertyTypeMaster> propTypeMstrList = propertyTypeMasterDAO.findAll();
		for (PropertyTypeMaster propTypeMstr : propTypeMstrList) {
			propTypeMap.put(propTypeMstr.getId().toString(), bean);
		}
		LOGGER.debug("PropTypeMap : " + (propTypeMap != null ? propTypeMap : ZERO));
		LOGGER.debug("Exit from initPropTypeMap method");
		return propTypeMap;
	}

}
