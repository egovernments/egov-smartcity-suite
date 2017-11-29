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
package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class EstimateAppropriationRegisterJSONAdaptor implements JsonSerializer<BudgetFolioDetail>{

    @Override
    public JsonElement serialize(final BudgetFolioDetail budgetFolioDetail, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (budgetFolioDetail != null) {
            if (budgetFolioDetail.getBudgetApprNo() != null)
                jsonObject.addProperty("appropriationNumber", budgetFolioDetail.getBudgetApprNo());
            else
                jsonObject.addProperty("appropriationNumber", "");
            if (budgetFolioDetail.getAppDate() != null)
                jsonObject.addProperty("appropriationDate", budgetFolioDetail.getAppDate());
            else
                jsonObject.addProperty("appropriationDate", "");
            if (budgetFolioDetail.getAppropriatedValue() != null)
                jsonObject.addProperty("appropriationValue", budgetFolioDetail.getAppropriatedValue());
            else
                jsonObject.addProperty("appropriationValue", "");
            if (budgetFolioDetail.getEstimateNo() != null)
                jsonObject.addProperty("estimateNumber", budgetFolioDetail.getEstimateNo());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (budgetFolioDetail.getWorkIdentificationNumber() != null)
                jsonObject.addProperty("workIdentificationNumber", budgetFolioDetail.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            if (budgetFolioDetail.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", budgetFolioDetail.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (budgetFolioDetail.getEstimateDate() != null)
                jsonObject.addProperty("estimateDate", budgetFolioDetail.getEstimateDate());
            else
                jsonObject.addProperty("estimateDate", "");
            if (budgetFolioDetail.getWorkValue() != null)
                jsonObject.addProperty("estimateValue", budgetFolioDetail.getWorkValue());
            else
                jsonObject.addProperty("estimateValue", "");
            if(budgetFolioDetail.getCumulativeTotal() != null)
                jsonObject.addProperty("cumulativeTotal", budgetFolioDetail.getCumulativeTotal());
            else
                jsonObject.addProperty("cumulativeTotal", "");
            if(budgetFolioDetail.getBalanceAvailable() != null)
                jsonObject.addProperty("balanceAvailable", budgetFolioDetail.getBalanceAvailable());
            else
                jsonObject.addProperty("balanceAvailable", "");
            if(budgetFolioDetail.getActualBalanceAvailable() != null)
                jsonObject.addProperty("actualBalanceAvailable", budgetFolioDetail.getActualBalanceAvailable());
            else
                jsonObject.addProperty("actualBalanceAvailable", "");
            if(budgetFolioDetail.getCumulativeExpensesIncurred() != null)
                jsonObject.addProperty("cumulativeExpensesIncurred", budgetFolioDetail.getCumulativeExpensesIncurred());
            else
                jsonObject.addProperty("cumulativeExpensesIncurred", "");
            
            jsonObject.addProperty("id", budgetFolioDetail.getSrlNo());
        }
        return jsonObject;
    }

}
