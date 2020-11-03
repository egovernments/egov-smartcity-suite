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

package org.egov.ptis.web.controller.reports;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.view.InstDmdCollInfo;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.service.utils.PropertyTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

@Component
public class BaseRegisterVLTResultAdaptor implements DataTableJsonAdapter<PropertyMVInfo> {
    private static final String ARR_LIB_CESS = "arrLibCess";
    private static final String CURR_SECOND_HALF_LIB_CESS = "currSecondHalfLibCess";
    private static final String CURR_FIRST_HALF_LIB_CESS = "currFirstHalfLibCess";
    public static final String CURRENTYEAR_FIRST_HALF = "Current 1st Half";
    public static final String CURRENTYEAR_SECOND_HALF = "Current 2nd Half";

    private static PropertyTaxUtil propertyTaxUtil;
    private static PropertyTaxService propertyTaxService;

    public BaseRegisterVLTResultAdaptor() {
    }

    @Autowired
    public BaseRegisterVLTResultAdaptor(final PropertyTaxUtil propertyTaxUtil, final PropertyTaxService propertyTaxService) {
        BaseRegisterVLTResultAdaptor.propertyTaxUtil = propertyTaxUtil;
        BaseRegisterVLTResultAdaptor.propertyTaxService = propertyTaxService;
    }

    @Override
    public JsonElement serialize(final DataTable<PropertyMVInfo> baseRegisterResponse, final Type type,
            final JsonSerializationContext jsc) {
        final List<PropertyMVInfo> baseRegisterResult = baseRegisterResponse.getData();
        final JsonArray baseRegisterResultData = new JsonArray();
        baseRegisterResult.forEach(baseRegisterResultObj -> {
            final JsonObject jsonObject = new JsonObject();

            final BigDecimal taxRate = propertyTaxUtil.getTaxRates();
            final Map<String, BigDecimal> valuesMap = getTaxDetails(baseRegisterResultObj);

            final BigDecimal marketValue = baseRegisterResultObj.getMarketValue() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getMarketValue();
            final BigDecimal capitalValue = baseRegisterResultObj.getCapitalValue() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getCapitalValue();

            final BigDecimal higherValueForImposedTax = marketValue.compareTo(capitalValue) > 0
                    ? marketValue.setScale(2, BigDecimal.ROUND_HALF_UP)
                    : capitalValue.setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal currPenaltyFine = BigDecimal.ZERO;
            if (baseRegisterResultObj.getAggrCurrFirstHalfPenaly() != null)
                currPenaltyFine = currPenaltyFine.add(baseRegisterResultObj.getAggrCurrFirstHalfPenaly());
            if (baseRegisterResultObj.getAggrCurrSecondHalfPenaly() != null)
                currPenaltyFine = currPenaltyFine.add(baseRegisterResultObj.getAggrCurrSecondHalfPenaly());

            final BigDecimal currentColl = baseRegisterResultObj.getAggrCurrFirstHalfColl() == null
                    ? BigDecimal.ZERO.add(((baseRegisterResultObj.getAggrCurrSecondHalfColl() == null)
                            ? BigDecimal.ZERO : baseRegisterResultObj.getAggrCurrSecondHalfColl()))
                    : baseRegisterResultObj.getAggrCurrFirstHalfColl().add(baseRegisterResultObj.getAggrCurrSecondHalfColl());

            final BigDecimal arrColl = baseRegisterResultObj.getAggrArrColl() != null
                    ? baseRegisterResultObj.getAggrArrColl() : BigDecimal.ZERO;
            final BigDecimal totalColl = arrColl.add(currentColl);
            final BigDecimal currTotal = baseRegisterResultObj.getAggrCurrFirstHalfDmd() == null
                    ? BigDecimal.ZERO.add(baseRegisterResultObj.getAggrCurrSecondHalfDmd() == null
                            ? BigDecimal.ZERO : baseRegisterResultObj.getAggrCurrSecondHalfDmd())
                    : baseRegisterResultObj.getAggrCurrFirstHalfDmd();
            jsonObject.addProperty("assessmentNo", baseRegisterResultObj.getPropertyId());
            jsonObject.addProperty("oldAssessmentNo",
                    StringUtils.isNotBlank(baseRegisterResultObj.getOldMuncipalNum())
                            ? baseRegisterResultObj.getOldMuncipalNum() : "NA");
			jsonObject.addProperty("sitalArea", baseRegisterResultObj.getSitalArea() != null
					? baseRegisterResultObj.getSitalArea().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            jsonObject.addProperty("ward", baseRegisterResultObj.getWard().getBoundaryNum());
            jsonObject.addProperty("ownerName", baseRegisterResultObj.getOwnerName());
            jsonObject.addProperty("surveyNo", StringUtils.isNotBlank(baseRegisterResultObj.getSurveyNo())
                    ? baseRegisterResultObj.getSurveyNo() : "NA");
            jsonObject.addProperty("taxationRate", taxRate);
            jsonObject.addProperty("marketValue", marketValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            jsonObject.addProperty("documentValue", capitalValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            jsonObject.addProperty("higherValueForImposedtax", higherValueForImposedTax.toString());
            jsonObject.addProperty("isExempted", baseRegisterResultObj.getIsExempted() ? "Yes" : "No");
            jsonObject.addProperty("propertyTaxFirstHlf", baseRegisterResultObj.getAggrCurrFirstHalfDmd() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getAggrCurrFirstHalfDmd());
            jsonObject.addProperty("waivedOffPT", baseRegisterResultObj.getWaivedoffAmount() != null ? baseRegisterResultObj.getWaivedoffAmount() : BigDecimal.ZERO);
            jsonObject.addProperty("courtVerdictAndWOAmount", propertyTaxService.getCourtVerdictAndWriteOffAmount(baseRegisterResultObj));
            
            if (!valuesMap.isEmpty()) {
                jsonObject.addProperty("libraryCessTaxFirstHlf", valuesMap.get(CURR_FIRST_HALF_LIB_CESS) == null
                        ? BigDecimal.ZERO : valuesMap.get(CURR_FIRST_HALF_LIB_CESS));
                jsonObject.addProperty("libraryCessTaxSecondHlf", valuesMap.get(CURR_SECOND_HALF_LIB_CESS) == null
                        ? BigDecimal.ZERO : valuesMap.get(CURR_SECOND_HALF_LIB_CESS));
                jsonObject.addProperty("arrearLibraryTax",
                        valuesMap.get(ARR_LIB_CESS) == null ? BigDecimal.ZERO : valuesMap.get(ARR_LIB_CESS));
            }

            jsonObject.addProperty("propertyTaxSecondHlf", baseRegisterResultObj.getAggrCurrSecondHalfDmd() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getAggrCurrSecondHalfDmd());

            jsonObject.addProperty("currTotal", currTotal);

            jsonObject.addProperty("penaltyFines", currPenaltyFine);
            jsonObject.addProperty("arrearPeriod", propertyTaxService.getArrearsPeriod(baseRegisterResultObj));
            jsonObject.addProperty("arrearPropertyTax",
                    baseRegisterResultObj.getAggrArrDmd() != null
                            && baseRegisterResultObj.getAggrArrDmd().compareTo(BigDecimal.ZERO) >= 1
                                    ? baseRegisterResultObj.getAggrArrDmd().subtract(
                                            valuesMap == null ? BigDecimal.ZERO : valuesMap.get(ARR_LIB_CESS))
                                    : BigDecimal.ZERO);
            jsonObject.addProperty("arrearPenaltyFines", baseRegisterResultObj.getAggrArrearPenaly() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getAggrArrearPenaly());
            jsonObject.addProperty("arrearTotal", baseRegisterResultObj.getAggrArrDmd() == null
                    ? BigDecimal.ZERO : baseRegisterResultObj.getAggrArrDmd());
            jsonObject.addProperty("arrearColl", arrColl);

            jsonObject.addProperty("currentColl", currentColl);
            jsonObject.addProperty("totalColl", totalColl);
            baseRegisterResultData.add(jsonObject);
        });
        return enhance(baseRegisterResultData, baseRegisterResponse);
    }

    private Map<String, BigDecimal> getTaxDetails(final PropertyMVInfo propMatView) {
        final List<InstDmdCollInfo> instDemandCollList = new LinkedList<>(propMatView.getInstDmdColl());
        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        final Map<String, BigDecimal> values = new LinkedHashMap<>();
        for (final InstDmdCollInfo instDmdCollObj : instDemandCollList)
            if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF).getId()))
                values.put(CURR_FIRST_HALF_LIB_CESS,
                        instDmdCollObj.getLibCessTax() == null ? BigDecimal.ZERO : instDmdCollObj.getLibCessTax());
            else if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getId()))
                values.put(CURR_SECOND_HALF_LIB_CESS,
                        instDmdCollObj.getLibCessTax() == null ? BigDecimal.ZERO : instDmdCollObj.getLibCessTax());
            else
                values.put(ARR_LIB_CESS,
                        instDmdCollObj.getLibCessTax() == null ? BigDecimal.ZERO : instDmdCollObj.getLibCessTax());
        return values;

    }

}
