package org.egov.ptis.nmc.util;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ORDERED_DEMAND_RSNS_LIST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

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
		reasonList.remove(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);
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
