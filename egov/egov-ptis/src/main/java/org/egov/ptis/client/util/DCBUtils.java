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
package org.egov.ptis.client.util;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.ptis.constants.PropertyTaxConstants;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ORDERED_DEMAND_RSNS_LIST;

public class DCBUtils {

	private final Logger LOGGER = Logger.getLogger(getClass());

	/**
	 * To set DcbDispInfo with ReasonCategoryCodes as Tax and Penalty. Here
	 * reasonMasterCodes could also be set to DcbDispInfo.
	 * 
	 * @return DCBDisplayInfo
	 */

	@SkipValidation
	public DCBDisplayInfo prepareDisplayInfo() {
		DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
		LOGGER.debug("Entered into method prepareDisplayInfo");
		List<String> reasonCategoryCodes = new ArrayList<String>();
		reasonCategoryCodes.add(PropertyTaxConstants.REASON_CATEGORY_CODE_TAX);
		reasonCategoryCodes.add(PropertyTaxConstants.REASON_CATEGORY_CODE_PENALTY);
		reasonCategoryCodes.add(PropertyTaxConstants.REASON_CATEGORY_CODE_FINES);
		reasonCategoryCodes.add(PropertyTaxConstants.REASON_CATEGORY_CODE_ADVANCE);

		dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
		List<String> reasonList = new ArrayList<String>();
		reasonList.addAll(ORDERED_DEMAND_RSNS_LIST);
		reasonList.remove(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);
		dcbDispInfo.setReasonMasterCodes(reasonList);
		LOGGER.debug("DCB Display Info : " + dcbDispInfo);
		LOGGER.debug("Number of Demand Reasons : " + (reasonList != null ? reasonList.size() : ZERO));
		LOGGER.debug("Exit from method prepareDisplayInfo");
		return dcbDispInfo;
	}

	@SkipValidation
	public DCBDisplayInfo prepareDisplayInfoHeadwise() {

		LOGGER.debug("Entered into method prepareDisplayInfo");
		DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
		List<String> reasonCategoryCodes = new ArrayList<String>();
		reasonCategoryCodes.add(PropertyTaxConstants.REASON_CATEGORY_CODE_ADVANCE);
		dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
		List<String> reasonList = new ArrayList<String>();
		reasonList.addAll(ORDERED_DEMAND_RSNS_LIST);
		dcbDispInfo.setReasonMasterCodes(reasonList);
		LOGGER.debug("DCB Display Info : " + dcbDispInfo);
		LOGGER.debug("Number of Demand Reasons : " + (reasonList != null ? reasonList.size() : ZERO));
		LOGGER.debug("Exit from method prepareDisplayInfo");
		return dcbDispInfo;
	}
}
