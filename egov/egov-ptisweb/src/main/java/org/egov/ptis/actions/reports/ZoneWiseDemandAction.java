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

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;

public class ZoneWiseDemandAction extends ActionSupport {
	private static final Logger LOGGER = Logger.getLogger(ZoneWiseDemandAction.class);
	@Autowired
	private PropertyDAO propertyDAO;

	@Override
	public String execute() {
		LOGGER.debug("Entered into execute method");
		String target = "failure";
		List zoneDemandList;
		LinkedList<Map<String, Object>> links;
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			// criterion object consists of statements that needs to done in
			// where clause
			Criterion criterion = null;
			// Projection object consists of the fields that are required in
			// select statements
			Projection projection = Projections.projectionList()
					.add(Projections.property("zone.id")).add(Projections.sum("aggrArrDmd"))
					.add(Projections.sum("aggrCurrDmd")).add(Projections.groupProperty("zone.id"));
			// In Order object we can mention the order in which result needs to
			// displayed.
			Order order = Order.asc("zone.id");
			zoneDemandList = propertyDAO.getPropMaterlizeViewList(projection, criterion, order);
			LOGGER.debug("Zone wise demand list : "
					+ (zoneDemandList != null ? zoneDemandList : ZERO));
			links = prepareDispTagList(zoneDemandList);
			request.setAttribute("links", links);
			target = "success";
		} catch (Exception e) {
			target = "failure";
			LOGGER.error("Error in ZoneWiseDemandAction : " + e.getMessage());
			throw new ApplicationRuntimeException("error in ZoneWiseDemandAction---------------", e);
		}
		LOGGER.debug("Exit from execute method");
		return target;
	}

	// This method returns the linked list which needs to added in Display tag
	// to view the results in jsp.
	public LinkedList<Map<String, Object>> prepareDispTagList(List zoneDemandList) {
		LOGGER.debug("Entered into prepareDispTagList method");
		LOGGER.debug("Zone wise demand list : " + (zoneDemandList != null ? zoneDemandList : ZERO));
		String zoneNumber = "";
		BigDecimal arrearDemand = BigDecimal.ZERO;
		BigDecimal currentDemand = BigDecimal.ZERO;
		BigDecimal totalCurrDemand = BigDecimal.ZERO;
		BigDecimal totalArrearDemand = BigDecimal.ZERO;
		BigDecimal totalDemand = BigDecimal.ZERO;
		BigDecimal grandTotalDemand = BigDecimal.ZERO;
		LinkedList<Map<String, Object>> links = new LinkedList<Map<String, Object>>();
		Map<String, Object> map;
		Map<String, Object> totalsMap = new Hashtable<String, Object>();
		if (zoneDemandList != null && !zoneDemandList.isEmpty()) {
			for (Object object : zoneDemandList) {
				map = new HashMap<String, Object>();
				zoneNumber = "";
				arrearDemand = BigDecimal.ZERO;
				currentDemand = BigDecimal.ZERO;
				Object[] arrayObject = (Object[]) object;

				if (arrayObject[0] != null) {
					zoneNumber = arrayObject[0].toString();
					LOGGER.debug("Zone number : " + zoneNumber);
				}
				if (arrayObject[1] != null) {
					arrearDemand = (BigDecimal) arrayObject[1];
					LOGGER.debug("Arrear Demand : " + arrearDemand);
					totalArrearDemand = totalArrearDemand.add(arrearDemand);
				}
				if (arrayObject[2] != null) {
					currentDemand = (BigDecimal) arrayObject[2];
					LOGGER.debug("Current Demand : " + currentDemand);
					totalCurrDemand = totalCurrDemand.add(currentDemand);
				}
				totalDemand = arrearDemand.add(currentDemand);
				LOGGER.debug("Total Demand : " + totalDemand);
				grandTotalDemand = grandTotalDemand.add(totalDemand);
				map.put("zoneNumber", zoneNumber);
				map.put("totalArrearsDemand(Rs.)", arrearDemand);
				map.put("totalCurrentDemand(Rs.)", currentDemand);
				map.put("totalDemand(Rs.)", totalDemand);
				links.add(map);
			}
			totalsMap.put("zoneNumber", "Total(Rs.)");
			totalsMap.put("totalArrearsDemand(Rs.)", totalArrearDemand);
			LOGGER.debug("Total Arrear Demand : " + totalArrearDemand);
			totalsMap.put("totalCurrentDemand(Rs.)", totalCurrDemand);
			LOGGER.debug("Total Current Demand : " + totalCurrDemand);
			totalsMap.put("totalDemand(Rs.)", grandTotalDemand);
			LOGGER.debug("Grand Total Demand : " + grandTotalDemand);
			links.add(totalsMap);
		}
		LOGGER.debug("Exit from prepareDispTagList method");
		return links;
	}
}
