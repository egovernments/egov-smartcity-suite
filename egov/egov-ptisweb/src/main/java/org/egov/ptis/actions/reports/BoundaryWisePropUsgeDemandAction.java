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
import org.apache.struts2.ServletActionContext;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigDecimal.ZERO;

/**
 * This Action class is used to generate the a report called Zone wise Property
 * Usage Demand
 * 
 * @author Sathish Reddy
 * @version 2.00
 */

public class BoundaryWisePropUsgeDemandAction extends BaseFormAction {

	// private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BoundaryWisePropUsgeDemandAction.class);
	@Autowired
	private PropertyUsageDAO propertyUsageDAO;
	@Autowired
	private PropertyTypeMasterDAO propertyTypeMasterDAO;
	List bndryPropUseList = new ArrayList();
	private Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> bndryMap;
	private Map<Integer, Map<String, BoundryWisePropUsgeBean>> zoneMap;
	private transient BoundaryWisePropUsgeDelegate bndryDel;
	private Map parameters;
	private String bndryParamStr = null;
	private String wardParam = null;
	private String isPrint = null;

	@Override
	@SuppressWarnings("unchecked")
	public String execute() throws ApplicationException {
		LOGGER.debug("Entered into execute method");
		String target = "failure";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			bndryParamStr = getParameterValue("bndryParam");
			isPrint = getParameterValue("isPrint");
			LOGGER.debug("Boundary Param String : " + bndryParamStr + ", " + "Is Print ? : "
					+ isPrint);
			bndryDel = new BoundaryWisePropUsgeDelegate();
			List<PropertyTypeMaster> propMstrList = propertyTypeMasterDAO.findAll();
			// If bndryParamStr is null then the list we get is zoneList
			// Any value i.e zone number then the list we get is ward list
			if (bndryParamStr == null) {
				List zoneList = bndryDel.getZoneList();
				bndryMap = bndryDel.getBoundaryWiseList(zoneList, null, propMstrList);
				if (bndryMap != null) {
					zoneMap = bndryDel.getZoneAndPropertyTypeWiseList(bndryMap);
				} else {
					LOGGER.debug("Error in getting boundary wise list");
				}

			} else {
				List wardList = bndryDel.getWardList(Integer.parseInt(bndryParamStr));
				bndryMap = bndryDel.getBoundaryWiseList(wardList, Integer.parseInt(bndryParamStr),
						propMstrList);
				if (bndryMap != null) {
					zoneMap = bndryDel.getZoneAndPropertyTypeWiseList(bndryMap);
				} else {
					LOGGER.debug("Error in getting boundary wise list");
				}
				request.setAttribute("bndryParamStr", bndryParamStr);
			}
			LOGGER.debug("Boundary Map size : " + (bndryMap != null ? bndryMap.size() : ZERO)
					+ ", " + "Zone Map size : " + (zoneMap != null ? zoneMap.size() : ZERO));
			if (isPrint != null && isPrint.equals("TRUE")) {
				target = "printResult";
			} else {
				target = INDEX;
			}
			LOGGER.info("zoneMap=========" + zoneMap);
			request.setAttribute("zoneMap", zoneMap);
			request.setAttribute("bndryPropUsgeDelgte", bndryDel);

		} catch (Exception e) {
			target = "failure";
			LOGGER.error("--error in BoundaryWisePropUsgeDemand-in execute() method--"
					+ e.getMessage());
			throw new ApplicationRuntimeException("Error in ZoneWiseDemandAction---------------", e);
		}
		LOGGER.debug("Exit from execute method");
		return target;
	}

	public Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> getBndryMap() {
		return bndryMap;
	}

	public void setBndryMap(Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> bndryMap) {
		this.bndryMap = bndryMap;
	}

	public List getBndryPropUseList() {
		return bndryPropUseList;
	}

	public void setBndryPropUseList(List bndryPropUseList) {
		this.bndryPropUseList = bndryPropUseList;
	}

	public Map<Integer, Map<String, BoundryWisePropUsgeBean>> getZoneMap() {
		return zoneMap;
	}

	public void setZoneMap(Map<Integer, Map<String, BoundryWisePropUsgeBean>> zoneMap) {
		this.zoneMap = zoneMap;
	}

	public String getParameterValue(String param) {
		Object varr = getParameters().get(param);
		if (varr == null)
			return null;
		return ((String[]) varr)[0];
	}

	public Map getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public BoundaryWisePropUsgeDelegate getBndryPropUsgeDelgte() {
		return bndryDel;
	}

	public void setBndryPropUsgeDelgte(BoundaryWisePropUsgeDelegate bndryPropUsgeDelgte) {
		this.bndryDel = bndryPropUsgeDelgte;
	}

	public String getBndryParamStr() {
		return bndryParamStr;
	}

	public void setBndryParamStr(String bndryParamStr) {
		this.bndryParamStr = bndryParamStr;
	}

	@Override
	public Object getModel() {

		return null;
	}

	public String getWardParam() {
		return wardParam;
	}

	public void setWardParam(String wardParam) {
		this.wardParam = wardParam;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
}
