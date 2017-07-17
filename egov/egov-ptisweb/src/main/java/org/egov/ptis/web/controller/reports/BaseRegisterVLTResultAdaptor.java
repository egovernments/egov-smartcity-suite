/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.ptis.web.controller.reports;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.egov.commons.Installment;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.view.InstDmdCollInfo;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

@Component
public class BaseRegisterVLTResultAdaptor implements DataTableJsonAdapter<PropertyMVInfo> {
	public static final String CURRENTYEAR_FIRST_HALF = "Current 1st Half";
	public static final String CURRENTYEAR_SECOND_HALF = "Current 2nd Half";

	private Properties taxRateProps = null;

	private static PropertyTaxUtil propertyTaxUtil;

	public BaseRegisterVLTResultAdaptor() {
	}

	@Autowired
	public BaseRegisterVLTResultAdaptor(final PropertyTaxUtil propertyTaxUtil) {
		BaseRegisterVLTResultAdaptor.propertyTaxUtil = propertyTaxUtil;
	}

	@Override
	public JsonElement serialize(final DataTable<PropertyMVInfo> baseRegisterResponse, final Type type,
			final JsonSerializationContext jsc) {
		final List<PropertyMVInfo> baseRegisterResult = baseRegisterResponse.getData();
		final JsonArray baseRegisterResultData = new JsonArray();
		baseRegisterResult.forEach(baseRegisterResultObj -> {
			final JsonObject jsonObject = new JsonObject();

			final BigDecimal taxRate = getTaxRate("VAC_LAND_TAX");
			final Map<String, BigDecimal> valuesMap = getTaxDetails(baseRegisterResultObj);

			final BigDecimal marketValue = baseRegisterResultObj.getMarketValue() != null
					? baseRegisterResultObj.getMarketValue() : BigDecimal.ZERO;
			final BigDecimal capitalValue = baseRegisterResultObj.getCapitalValue() != null
					? baseRegisterResultObj.getCapitalValue() : BigDecimal.ZERO;

			final BigDecimal higherValueForImposedTax = marketValue.compareTo(capitalValue) > 0
					? marketValue.setScale(2, BigDecimal.ROUND_HALF_UP)
					: capitalValue.setScale(2, BigDecimal.ROUND_HALF_UP);

			BigDecimal currPenaltyFine = BigDecimal.ZERO;
			if (baseRegisterResultObj.getAggrCurrFirstHalfPenaly() != null)
				currPenaltyFine = currPenaltyFine.add(baseRegisterResultObj.getAggrCurrFirstHalfPenaly());
			if (baseRegisterResultObj.getAggrCurrSecondHalfPenaly() != null)
				currPenaltyFine = currPenaltyFine.add(baseRegisterResultObj.getAggrCurrSecondHalfPenaly());

			final BigDecimal currentColl = baseRegisterResultObj.getAggrCurrFirstHalfColl() != null
					? baseRegisterResultObj.getAggrCurrFirstHalfColl()
					: BigDecimal.ZERO.add(baseRegisterResultObj.getAggrCurrSecondHalfColl() != null
							? baseRegisterResultObj.getAggrCurrSecondHalfColl() : BigDecimal.ZERO);

			final BigDecimal arrColl = baseRegisterResultObj.getAggrArrColl() != null
					? baseRegisterResultObj.getAggrArrColl() : BigDecimal.ZERO;
			final BigDecimal totalColl = arrColl.add(currentColl);
			final BigDecimal currTotal = baseRegisterResultObj.getAggrCurrFirstHalfDmd() != null
					? baseRegisterResultObj.getAggrCurrFirstHalfDmd()
					: BigDecimal.ZERO.add(baseRegisterResultObj.getAggrCurrSecondHalfDmd() != null
							? baseRegisterResultObj.getAggrCurrSecondHalfDmd() : BigDecimal.ZERO);
			jsonObject.addProperty("assessmentNo", baseRegisterResultObj.getPropertyId());
			jsonObject.addProperty("oldAssessmentNo",
					baseRegisterResultObj.getOldMuncipalNum() != null
							&& org.apache.commons.lang.StringUtils.isNotBlank(baseRegisterResultObj.getOldMuncipalNum())
									? baseRegisterResultObj.getOldMuncipalNum() : "NA");
			jsonObject.addProperty("sitalArea", baseRegisterResultObj.getSitalArea().setScale(2, BigDecimal.ROUND_HALF_UP));
			jsonObject.addProperty("ward", baseRegisterResultObj.getWard().getBoundaryNum());
			jsonObject.addProperty("ownerName", baseRegisterResultObj.getOwnerName());
			jsonObject.addProperty("surveyNo", baseRegisterResultObj.getSurveyNo() != null
					? baseRegisterResultObj.getSurveyNo().toString() : "NA");
			jsonObject.addProperty("taxationRate", taxRate);
			jsonObject.addProperty("marketValue", marketValue.toString());
			jsonObject.addProperty("documentValue", capitalValue.toString());
			jsonObject.addProperty("higherValueForImposedtax", higherValueForImposedTax.toString());
			jsonObject.addProperty("isExempted", baseRegisterResultObj.getIsExempted() ? "Yes" : "No");
			jsonObject.addProperty("propertyTaxFirstHlf", baseRegisterResultObj.getAggrCurrFirstHalfDmd() != null
					? baseRegisterResultObj.getAggrCurrFirstHalfDmd() : BigDecimal.ZERO);

			if (!valuesMap.isEmpty()) {
				jsonObject.addProperty("libraryCessTaxFirstHlf", valuesMap.get("currFirstHalfLibCess") != null
						? valuesMap.get("currFirstHalfLibCess") : BigDecimal.ZERO);
				jsonObject.addProperty("libraryCessTaxSecondHlf", valuesMap.get("currSecondHalfLibCess") != null
						? valuesMap.get("currSecondHalfLibCess") : BigDecimal.ZERO);
				jsonObject.addProperty("arrearLibraryTax",
						valuesMap.get("arrLibCess") != null ? valuesMap.get("arrLibCess") : BigDecimal.ZERO);
			}

			jsonObject.addProperty("propertyTaxSecondHlf", baseRegisterResultObj.getAggrCurrSecondHalfDmd() != null
					? baseRegisterResultObj.getAggrCurrSecondHalfDmd() : BigDecimal.ZERO);

			jsonObject.addProperty("currTotal", currTotal);

			jsonObject.addProperty("penaltyFines", currPenaltyFine);
			jsonObject.addProperty("arrearPeriod",
					baseRegisterResultObj.getDuePeriod() != null
							&& org.apache.commons.lang.StringUtils.isNotBlank(baseRegisterResultObj.getDuePeriod())
									? baseRegisterResultObj.getDuePeriod().toString() : "NA");
			jsonObject.addProperty("arrearPropertyTax",
					baseRegisterResultObj.getAggrArrDmd() != null
							&& baseRegisterResultObj.getAggrArrDmd().compareTo(BigDecimal.ZERO) >= 1
									? baseRegisterResultObj.getAggrArrDmd().subtract(valuesMap.get("arrLibCess"))
									: BigDecimal.ZERO);
			jsonObject.addProperty("arrearPenaltyFines", baseRegisterResultObj.getAggrArrearPenaly() != null
					? baseRegisterResultObj.getAggrArrearPenaly() : BigDecimal.ZERO);
			jsonObject.addProperty("arrearTotal", baseRegisterResultObj.getAggrArrDmd() != null
					? baseRegisterResultObj.getAggrArrDmd() : BigDecimal.ZERO);
			jsonObject.addProperty("arrearColl", arrColl);

			jsonObject.addProperty("currentColl", currentColl);
			jsonObject.addProperty("totalColl", totalColl);
			baseRegisterResultData.add(jsonObject);
		});
		return enhance(baseRegisterResultData, baseRegisterResponse);
	}

	private BigDecimal getTaxRate(final String taxHead) {
		taxRateProps = propertyTaxUtil.loadTaxRates();
		BigDecimal taxRate = BigDecimal.ZERO;
		if (taxRateProps != null)
			taxRate = new BigDecimal(taxRateProps.getProperty(taxHead));
		return taxRate;
	}

	private Map<String, BigDecimal> getTaxDetails(final PropertyMVInfo propMatView) {
		final List<InstDmdCollInfo> instDemandCollList = new LinkedList<>(propMatView.getInstDmdColl());
		final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
		final Map<String, BigDecimal> values = new LinkedHashMap<>();
		for (final InstDmdCollInfo instDmdCollObj : instDemandCollList)
			if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF)))
				values.put("currFirstHalfLibCess",
						instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
			else if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF)))
				values.put("currSecondHalfLibCess",
						instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
			else
				values.put("arrLibCess",
						instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
		return values;

	}

}
