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
package org.egov.restapi.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.model.budget.BudgetDetail;
import org.egov.restapi.model.BudgetAvailableResponse;
import org.egov.restapi.model.BudgetCheck;
import org.egov.restapi.model.BudgetDetailsResponse;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.service.BudgetCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget")
public class RestBudgetDetailController {

    @Autowired
    private BudgetCheckService budgetCheckService;

    @GetMapping(value = "/budgetdetails", produces = APPLICATION_JSON_VALUE)
    public BudgetAvailableResponse budgetAvailableDetails(@Valid final BudgetCheck budgetCheck) {
        List<BudgetDetailsResponse> response = new ArrayList<>();
        BudgetAvailableResponse budgetAvailableResponse = new BudgetAvailableResponse();
        List<BudgetDetail> budgetDetailresponse = budgetCheckService.getBudgetAvailableDetails(budgetCheck);
        setResponse(budgetDetailresponse, response, budgetAvailableResponse);
        return budgetAvailableResponse;
    }

    void setResponse(List<BudgetDetail> budgetDetailresponse, List<BudgetDetailsResponse> response,
            BudgetAvailableResponse budgetAvailableResponse) {
        Map<String, String> status = new HashMap<>();
        for (BudgetDetail bd : budgetDetailresponse) {
            BudgetDetailsResponse budgetDetailsResponse = new BudgetDetailsResponse();
            budgetDetailsResponse.setDepartmentName(bd.getExecutingDepartment().getName());
            budgetDetailsResponse.setFundName(bd.getFund().getName());
            budgetDetailsResponse.setFunctionName(bd.getFunction().getName());
            budgetDetailsResponse.setSchemeName(bd.getScheme() == null ? StringUtils.EMPTY : bd.getScheme().getName());
            budgetDetailsResponse.setSubschemeName(bd.getSubScheme() == null ? StringUtils.EMPTY : bd.getSubScheme().getName());
            budgetDetailsResponse.setBudgetHead(bd.getBudgetGroup().getName());
            budgetDetailsResponse
                    .setAvailableBalance(bd.getBudgetAvailable() == null ? BigDecimal.ZERO : bd.getBudgetAvailable());
            budgetDetailsResponse.setBudgetAllocated(bd.getApprovedAmount().add(
                    bd.getApprovedReAppropriationsTotal() == null ? BigDecimal.ZERO : bd.getApprovedReAppropriationsTotal()));
            budgetDetailsResponse.setBudgetUtilized(
                    budgetDetailsResponse.getBudgetAllocated().subtract(budgetDetailsResponse.getAvailableBalance()));
            response.add(budgetDetailsResponse);
        }
        if (response.isEmpty()) {
            status.put("Code", "FAILED");
            status.put("Message", "No data found");
            budgetAvailableResponse.setStatus(status);
        } else {
            status.put("Code", "SUCCESS");
            status.put("Message", "Total " + response.size() + " record found");
            budgetAvailableResponse.setStatus(status);
            budgetAvailableResponse.setBudgetDetailsResponse(response);
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public RestErrors restErrors(final RuntimeException runtimeException) {
        return new RestErrors("BUDGET DETAIL DOES NOT EXIST", runtimeException.getMessage());
    }
}