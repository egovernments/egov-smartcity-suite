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

import org.egov.commons.Installment;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.view.FloorDetailsInfo;
import org.egov.ptis.domain.entity.property.view.InstDmdCollInfo;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

@Component
public class BaseRegisterResultAdaptor implements DataTableJsonAdapter<PropertyMVInfo> {

	public static final String CURRENTYEAR_FIRST_HALF = "Current 1st Half";
	public static final String CURRENTYEAR_SECOND_HALF = "Current 2nd Half";

	private static PropertyTaxUtil propertyTaxUtil;

	public BaseRegisterResultAdaptor() {
	}

	@Autowired
	public BaseRegisterResultAdaptor(final PropertyTaxUtil propertyTaxUtil) {
		BaseRegisterResultAdaptor.propertyTaxUtil = propertyTaxUtil;
	}

	@Override
	public JsonElement serialize(final DataTable<PropertyMVInfo> baseRegisterResponse, final Type type,
			final JsonSerializationContext jsc) {
		final List<PropertyMVInfo> baseRegisterResult = baseRegisterResponse.getData();
		final JsonArray baseRegisterResultData = new JsonArray();
		baseRegisterResult.forEach(baseRegisterResultObj -> {
			final JsonObject jsonObject = new JsonObject();
			final Map<String, BigDecimal> valuesMap = getTaxDetails(baseRegisterResultObj);
			final Map<String, String> floorValuesMap = getFloorDetails(baseRegisterResultObj);

			BigDecimal currTotal = BigDecimal.ZERO;

			final BigDecimal currColl = baseRegisterResultObj.getAggrCurrFirstHalfColl() != null
					? baseRegisterResultObj.getAggrCurrFirstHalfColl()
					: BigDecimal.ZERO.add(baseRegisterResultObj.getAggrCurrSecondHalfColl() != null
							? baseRegisterResultObj.getAggrCurrSecondHalfColl() : BigDecimal.ZERO);

			final BigDecimal totalColl = currColl.add(baseRegisterResultObj.getArrearCollection() != null
					? baseRegisterResultObj.getArrearCollection() : BigDecimal.ZERO);
			if (!valuesMap.isEmpty())
				currTotal = valuesMap.get("totalCurrPropertyTax").add(valuesMap.get("totalCurrEduCess")
						.add(valuesMap.get("totalCurrLibCess").add(valuesMap.get("currentUACPenalty"))));

			jsonObject.addProperty("assessmentNo", baseRegisterResultObj.getPropertyId());
			jsonObject.addProperty("ownerName", baseRegisterResultObj.getOwnerName());
			jsonObject.addProperty("doorNo",
					baseRegisterResultObj.getHouseNo() != null ? baseRegisterResultObj.getHouseNo().toString() : "");
			jsonObject.addProperty("natureOfUsage", baseRegisterResultObj.getUsage());
			jsonObject.addProperty("exemption", baseRegisterResultObj.getIsExempted() ? "Yes" : "No");
			jsonObject.addProperty("courtCase", baseRegisterResultObj.getIsUnderCourtCase() ? "Yes" : "No");
			jsonObject.addProperty("arrearPeriod",
					baseRegisterResultObj.getDuePeriod() != null
							&& org.apache.commons.lang.StringUtils.isNotBlank(baseRegisterResultObj.getDuePeriod())
									? baseRegisterResultObj.getDuePeriod().toString() : "NA");
			jsonObject.addProperty("currentColl", currColl);
			jsonObject.addProperty("arrearTotal", baseRegisterResultObj.getArrearDemand() != null
					? baseRegisterResultObj.getArrearDemand() : BigDecimal.ZERO);
			jsonObject.addProperty("arrearPenaltyFines", baseRegisterResultObj.getAggrArrearPenaly() != null
					? baseRegisterResultObj.getAggrArrearPenaly() : BigDecimal.ZERO);
			jsonObject.addProperty("arrearColl", baseRegisterResultObj.getArrearCollection() != null
					? baseRegisterResultObj.getArrearCollection() : BigDecimal.ZERO);
			jsonObject.addProperty("totalColl", totalColl);

			if (!valuesMap.isEmpty()) {
				jsonObject.addProperty("generalTax", valuesMap.get("totalCurrPropertyTax"));
				jsonObject.addProperty("libraryCessTax", valuesMap.get("totalCurrLibCess"));
				jsonObject.addProperty("eduCessTax", valuesMap.get("totalCurrEduCess"));
				jsonObject.addProperty("penaltyFinesTax", valuesMap.get("currPenaltyFine"));
				jsonObject.addProperty("UACpenalty", valuesMap.get("currentUACPenalty"));
				jsonObject.addProperty("arrearUACPenalty", valuesMap.get("arrearUACPenalty"));

				jsonObject.addProperty("arrearPropertyTax", valuesMap.get("totalArrearPropertyTax"));
				jsonObject.addProperty("arrearlibCess", valuesMap.get("totalArreaLibCess"));
				jsonObject.addProperty("arrearEduCess", valuesMap.get("totalArrearEduCess"));
				jsonObject.addProperty("currTotal", currTotal);
			}
			if (!floorValuesMap.isEmpty()) {
				jsonObject.addProperty("propertyUsage", floorValuesMap.get("propertyUsage") != null
						? floorValuesMap.get("propertyUsage").toString() : "");
				jsonObject.addProperty("classification",
						floorValuesMap.get("classification") != null ? floorValuesMap.get("classification") : "");
				jsonObject.addProperty("area", floorValuesMap.get("area"));
			}

			baseRegisterResultData.add(jsonObject);
		});
		return enhance(baseRegisterResultData, baseRegisterResponse);
	}

	private Map<String, BigDecimal> getTaxDetails(final PropertyMVInfo propMatView) {

		BigDecimal totalArrearPropertyTax = BigDecimal.ZERO;
		BigDecimal totalArrearEduCess = BigDecimal.ZERO;
		BigDecimal totalArreaLibCess = BigDecimal.ZERO;
		BigDecimal arrearPenaltyFine = BigDecimal.ZERO;
		BigDecimal totalCurrPropertyTax = BigDecimal.ZERO;
		BigDecimal totalCurrEduCess = BigDecimal.ZERO;
		BigDecimal totalCurrLibCess = BigDecimal.ZERO;
		BigDecimal currPenaltyFine = BigDecimal.ZERO;
		BigDecimal currentUACPenalty = BigDecimal.ZERO;
		BigDecimal arrearUACPenalty = BigDecimal.ZERO;

		final List<InstDmdCollInfo> instDemandCollList = new LinkedList<>(propMatView.getInstDmdColl());
		final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
		final Map<String, BigDecimal> values = new LinkedHashMap<>();
		for (final InstDmdCollInfo instDmdCollObj : instDemandCollList)
			if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF).getId())) {
				totalCurrPropertyTax = totalCurrPropertyTax
						.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
				values.put("totalCurrPropertyTax", totalCurrPropertyTax);
				totalCurrEduCess = totalCurrEduCess
						.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
				values.put("totalCurrEduCess", totalCurrEduCess);
				totalCurrLibCess = totalCurrLibCess
						.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
				values.put("totalCurrLibCess", totalCurrLibCess);
				currPenaltyFine = currPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null
						? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
				values.put("currPenaltyFine", currPenaltyFine);
				currentUACPenalty = currentUACPenalty.add(instDmdCollObj.getUnauthPenaltyTax() != null
						? instDmdCollObj.getUnauthPenaltyTax() : BigDecimal.ZERO);
				values.put("currentUACPenalty", currentUACPenalty);

			} else if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getId())) {
				totalCurrPropertyTax = totalCurrPropertyTax
						.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
				values.put("totalCurrPropertyTax", totalCurrPropertyTax);
				totalCurrEduCess = totalCurrEduCess
						.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
				values.put("totalCurrEduCess", totalCurrEduCess);
				totalCurrLibCess = totalCurrLibCess
						.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
				values.put("totalCurrLibCess", totalCurrLibCess);
				currPenaltyFine = currPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null
						? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
				values.put("currPenaltyFine", currPenaltyFine);
				currentUACPenalty = currentUACPenalty.add(instDmdCollObj.getUnauthPenaltyTax() != null
						? instDmdCollObj.getUnauthPenaltyTax() : BigDecimal.ZERO);
				values.put("currentUACPenalty", currentUACPenalty);
				
			} else {
				totalArrearPropertyTax = totalArrearPropertyTax
						.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
				values.put("totalArrearPropertyTax", totalArrearPropertyTax);
				totalArrearEduCess = totalArrearEduCess
						.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
				values.put("totalArrearEduCess", totalArrearEduCess);
				totalArreaLibCess = totalArreaLibCess
						.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
				values.put("totalArreaLibCess", totalArreaLibCess);
				arrearPenaltyFine = arrearPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null
						? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
				values.put("arrearPenaltyFine", arrearPenaltyFine);
				arrearUACPenalty = arrearUACPenalty.add(instDmdCollObj.getUnauthPenaltyTax() != null
						? instDmdCollObj.getUnauthPenaltyTax() : BigDecimal.ZERO);
				values.put("arrearUACPenalty", arrearUACPenalty);
			}
		return values;
	}

	private Map<String, String> getFloorDetails(final PropertyMVInfo propMatView) {

		final List<FloorDetailsInfo> floorDetailsList = new LinkedList<>(propMatView.getFloorDetails());
		final Map<String, String> floorValues = new LinkedHashMap<>();

		if (floorDetailsList.size() > 1) {
			int count = 0;
			for (final FloorDetailsInfo floorDetailsObj : floorDetailsList)
				if (count == 0) {
					floorValues.put("propertyUsage", floorDetailsObj.getPropertyUsage());
					floorValues.put("classification", floorDetailsObj.getClassification());
					floorValues.put("area",
							floorDetailsObj.getBuiltUpArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					count++;
				} else {
					floorValues.put("propertyUsage", floorDetailsObj.getPropertyUsage());
					floorValues.put("classification", floorDetailsObj.getClassification());
					floorValues.put("area",
							floorDetailsObj.getBuiltUpArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				}
		} else
			for (final FloorDetailsInfo floorDetailsObj : floorDetailsList) {
				floorValues.put("propertyUsage", floorDetailsObj.getPropertyUsage());
				floorValues.put("classification", floorDetailsObj.getClassification());
				floorValues.put("area",
						floorDetailsObj.getBuiltUpArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			}
		return floorValues;
	}
}
